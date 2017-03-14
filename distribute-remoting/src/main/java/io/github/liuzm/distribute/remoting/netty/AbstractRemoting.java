package io.github.liuzm.distribute.remoting.netty;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.liuzm.distribute.common.model.Node;
import io.github.liuzm.distribute.registy.RegistryNode;
import io.github.liuzm.distribute.registy.RegistryNodeFactory;
import io.github.liuzm.distribute.remoting.InvokeCallback;
import io.github.liuzm.distribute.remoting.Processor;
import io.github.liuzm.distribute.remoting.common.Pair;
import io.github.liuzm.distribute.remoting.common.SemaphoreReleaseOnlyOnce;
import io.github.liuzm.distribute.remoting.protocol.Command;
import io.github.liuzm.distribute.remoting.protocol.HeaderMessageCode;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

/**
 * 
 * @author xh-liuzhimin
 *
 */
public abstract class AbstractRemoting {

	private static final Logger logger = LoggerFactory.getLogger(AbstractRemoting.class);
	
	/**
	 * 应答表，存储对应的server和client的连接
	 */
	protected final ConcurrentHashMap<Integer, FutureResponse> responseTable = new ConcurrentHashMap<Integer, FutureResponse>(
			256);
	protected final HashMap<Integer, Pair<Processor, ExecutorService>> processorTable = new HashMap<Integer, Pair<Processor, ExecutorService>>(
			64);
	protected Pair<Processor, ExecutorService> defaultProcessor;
	protected ExecutorService defaultExecutor;
    protected final Semaphore semaphoreAsync = new Semaphore(1, true);
    
    protected int nodeType = 0;
    protected  RegistryNode registryNode;
    
    abstract public RegistryNodeFactory getRegisterNodeFactory();
    
    public AbstractRemoting(final int nodeType,final ServerConfig config){
    	this.nodeType = nodeType;
    	
    	Node node = new Node();
    	node.setType(nodeType);
    	if(nodeType == 0){
    		node.setPort(config.getListenPort());
    	}
		this.registryNode = getRegisterNodeFactory().getRegistryNode(node);
		node = registryNode.register(node);
    }
    
	
	abstract public ExecutorService getOwnExecutor();
	/**
	 * remoting request
	 * 
	 * @param ctx
	 * @param cmd
	 */
	public void processRequestCommand(final ChannelHandlerContext ctx, final Command cmd) {

		final Pair<Processor, ExecutorService> matched = this.processorTable.get(cmd.getCode());
		final Pair<Processor, ExecutorService> pair = null == matched ? this.defaultProcessor : matched;
		if (pair != null) {
			Runnable run = new Runnable() {
				@Override
				public void run() {
					try {
						final Command response = pair.getObject1().process(ctx, cmd);
						if (response != null) {
							response.setOpaque(cmd.getOpaque());
                            response.markResponseType();
							ctx.writeAndFlush(response);
						}
					} catch (Exception e) {
						e.printStackTrace();
						final Command response = Command.createResponseCommand(HeaderMessageCode.SYSTEM_ERROR, "processor execute error");
						response.setOpaque(cmd.getOpaque());
                        ctx.writeAndFlush(response);
					}
				}
			};
			try {
				final RequestTask requestTask = new RequestTask(run, ctx.channel(), cmd);
                pair.getObject2().submit(requestTask);
				
			} catch (Exception e) {
				e.printStackTrace();
				final Command response = Command.createResponseCommand(HeaderMessageCode.SYSTEM_ERROR, "processor execute error");
				response.setOpaque(cmd.getOpaque());
                ctx.writeAndFlush(response);
			}
		}else{
			String error = " request type " + cmd.getCode() + " not supported";
			final Command response = Command.createResponseCommand(HeaderMessageCode.SYSTEM_ERROR, error);
			response.setOpaque(cmd.getOpaque());
            ctx.writeAndFlush(response);
		}
	}

	/**
	 * remoting response
	 * 
	 * @param ctx
	 * @param cmd
	 */
	public void processResponseCommand(ChannelHandlerContext ctx, Command cmd) {

		final FutureResponse responseFuture = responseTable.get(cmd.getOpaque());
		if (responseFuture != null) {
			responseFuture.setResponseCommand(cmd);
			responseFuture.release();
			responseTable.remove(cmd.getOpaque());
			
			if (responseFuture.getInvokeCallback() != null) {
				// 如果回调的话执行
				boolean runInThisThread = false;
                ExecutorService executor = this.getOwnExecutor();
                if (executor != null) {
                    try {
                        executor.submit(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    responseFuture.executeInvokeCallback();
                                }
                                catch (Throwable e) {
                                	logger.warn("excute callback in executor exception, and callback throw", e);
                                }
                            }
                        });
                    }
                    catch (Exception e) {
                        runInThisThread = true;
                        logger.warn("excute callback in executor exception, maybe executor busy", e);
                    }
                }
                else {
                    runInThisThread = true;
                }

                if (runInThisThread) {
                    try {
                        responseFuture.executeInvokeCallback();
                    }
                    catch (Throwable e) {
                    	logger.warn("executeInvokeCallback Exception", e);
                    }
                }
			} else {
				//
				responseFuture.putResponse(cmd);
			}
		} else {
			logger.warn("receive response, but not matched any request, " + "server ip + client ip ");
			logger.warn(cmd.toString());
		}

	}

	/**
	 * 
	 * @param ctx
	 * @param msg
	 * @throws Exception
	 */
	public void processMessageReceived(ChannelHandlerContext ctx, Command msg) throws Exception {
		final Command cmd = msg;
		if (cmd != null) {
			switch (cmd.getType()) {
				case REQUEST_COMMAND:
					processRequestCommand(ctx, cmd);
					break;
				case RESPONSE_COMMAND:
					processResponseCommand(ctx, cmd);
					break;
				default:
					break;
			}
		}
	}
	/**
	 * 同步请求发送实现
	 * 
	 * @param channel
	 * @param request
	 * @param timeoutMillis
	 * @return
	 * @throws InterruptedException
	 */
	public Command invokeSyncImpl(final Channel channel, final Command request, final long timeoutMillis)
			throws InterruptedException {
		final FutureResponse future = new FutureResponse(request.getOpaque(), timeoutMillis, null, null);
		this.responseTable.put(request.getOpaque(), future);
		channel.writeAndFlush(request).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture f) throws Exception {
                if (f.isSuccess()) {
                	future.setSendRequestOK(true);
                    return;
                }
                else {
                	future.setSendRequestOK(false);
                }

                responseTable.remove(request.getOpaque());
                future.setCause(f.cause());
                future.putResponse(null);
            }
        });
		Command response = future.waitResponse(timeoutMillis);
		return response;
	}

	/**
	 * 异步发送，不需要立即返回
	 * @throws InterruptedException 
	 */
	public void invokeAsyncImpl(final Channel channel, final Command request, final long timeoutMillis,
			final InvokeCallback invokeCallback) throws InterruptedException {
		
		boolean acquired = this.semaphoreAsync.tryAcquire(timeoutMillis, TimeUnit.MILLISECONDS);
		final int opaque = request.getOpaque();
		
		if(acquired){
			final SemaphoreReleaseOnlyOnce once = new SemaphoreReleaseOnlyOnce(this.semaphoreAsync);
			final FutureResponse responseFuture = new FutureResponse(opaque, timeoutMillis, invokeCallback,once);
			this.responseTable.put(request.getOpaque(), responseFuture);
			try {
				channel.writeAndFlush(request).addListener(new ChannelFutureListener() {
					@Override
					public void operationComplete(ChannelFuture f) throws Exception {
						if (f.isSuccess()) {
							responseFuture.setSendRequestOK(true);
							return;
						} else {
							responseFuture.setSendRequestOK(false);
						}
	
						responseFuture.putResponse(null);
						responseTable.remove(request.getOpaque());
						try {
							responseFuture.executeInvokeCallback();
						} catch (Throwable e) {
							logger.warn("excute callback in writeAndFlush addListener, and callback throw", e);
						} finally {
							responseFuture.release();
						}
	
						logger.warn("send a request command to channel <{}> failed.", channel);
					}
				});
			} catch (Exception e) {
				responseFuture.release();
			}
		}
	}
	
	public void scanResponseTable() {
        final List<FutureResponse> rfList = new LinkedList<FutureResponse>();
        Iterator<Entry<Integer, FutureResponse>> it = this.responseTable.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Integer, FutureResponse> next = it.next();
            FutureResponse rep = next.getValue();

            if ((rep.getBeginTimestamp() + rep.getTimeoutMillis() + 1000) <= System.currentTimeMillis()) {
                rep.release();
                it.remove();
                rfList.add(rep);
                logger.warn("remove timeout request, " + rep);
            }
        }

        for (FutureResponse rf : rfList) {
            try {
                executeInvokeCallback(rf);
            } catch (Throwable e) {
            	logger.warn("scanResponseTable, operationComplete Exception", e);
            }
        }
    }
	
	
	private void executeInvokeCallback(final FutureResponse responseFuture) {
        boolean runInThisThread = false;
        ExecutorService executor = this.getCallbackExecutor();
        if (executor != null) {
            try {
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            responseFuture.executeInvokeCallback();
                        } catch (Throwable e) {
                            logger.warn("execute callback in executor exception, and callback throw", e);
                        }
                    }
                });
            } catch (Exception e) {
                runInThisThread = true;
                logger.warn("execute callback in executor exception, maybe executor busy", e);
            }
        } else {
            runInThisThread = true;
        }

        if (runInThisThread) {
            try {
                responseFuture.executeInvokeCallback();
            } catch (Throwable e) {
            	logger.warn("executeInvokeCallback Exception", e);
            }
        }
    }
	
	 abstract public ExecutorService getCallbackExecutor();
	

}
