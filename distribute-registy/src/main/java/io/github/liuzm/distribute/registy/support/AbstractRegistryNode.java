/**
 * 
 */
package io.github.liuzm.distribute.registy.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.liuzm.distribute.common.model.Node;
import io.github.liuzm.distribute.common.util.ConcurrentHashSet;
import io.github.liuzm.distribute.common.util.NamedThreadFactory;
import io.github.liuzm.distribute.registy.RegistryNode;
import io.netty.util.internal.StringUtil;

/**
 * @author xh-liuzhimin
 *
 */
public abstract class AbstractRegistryNode implements RegistryNode {

	private static final Logger logger = LoggerFactory.getLogger(AbstractRegistryNode.class);

	private final ExecutorService registryCacheExecutor = Executors.newFixedThreadPool(1,
			new NamedThreadFactory("SaveRegistryNodeCache"));

	protected Node registryNode;

	private final boolean syncSaveFile = false;

	private File file;

	private final AtomicLong lastCacheChangedtime = new AtomicLong();

	private final Set<Node> registered = new ConcurrentHashSet<Node>();

	private final Properties properties = new Properties();

	/**
	 * 
	 * @param node
	 */
	public AbstractRegistryNode(Node node) {
		setNode(node);

		String filename = System.getProperty("user.home") + "/.distribute/distribute-registry-node"
				+ node.getIpaddress() + "-" + node.getType() + ".cache";
		File file = null;
		if (!StringUtil.isNullOrEmpty(filename)) {
			file = new File(filename);
			if (!file.exists() && file.getParentFile() != null && !file.getParentFile().exists()) {
				if (!file.getParentFile().mkdirs()) {
					throw new IllegalArgumentException("Invalid registry store file " + file
							+ ", cause: Failed to create directory " + file.getParentFile() + "!");
				}
			}
		}
		this.file = file;
		loadProperties();
		// notify(node.getBackupUrls());
	}

	public void destroy() {
		if (logger.isInfoEnabled()) {
			logger.info("Destroy registry node:" + getNode());
		}
		Set<Node> destroyRegistered = new HashSet<Node>(getRegistered());
		if (!destroyRegistered.isEmpty()) {
			for (Node node : new HashSet<Node>(getRegistered())) {
				try {
					unregister(node);
					if (logger.isInfoEnabled()) {
						logger.info("Destroy unregister url " + node);
					}
				} catch (Throwable t) {
					logger.warn("Failed to unregister url " + node + " to registry " + getNode()
							+ " on destroy, cause: " + t.getMessage(), t);
				}
			}
		}

	}

	public void unregister(Node node) {
		if (node == null) {
			throw new IllegalArgumentException("unregister url == null");
		}
		if (logger.isInfoEnabled()) {
			logger.info("Unregister: " + node);
		}
		registered.remove(node);
	}

	public Node getNode() {
		return registryNode;
	}

	public Set<Node> getRegistered() {
		return registered;
	}

	/**
	 * 
	 * @param node
	 */
	protected void setNode(Node node) {
		if (node == null) {
			throw new IllegalArgumentException("registryNode node == null");
		}
		this.registryNode = node;
	}

	private void saveProperties(Node node) {
		if (file == null) {
			return;
		}

		try {
			StringBuilder buf = new StringBuilder();
			properties.setProperty(node.getId(), buf.toString());
			long version = lastCacheChangedtime.incrementAndGet();
			if (syncSaveFile) {
				doSaveProperties(version);
			} else {
				registryCacheExecutor.execute(new SaveProperties(version));
			}
		} catch (Throwable t) {
			logger.warn(t.getMessage(), t);
		}
	}

	/**
	 * 
	 */
	private void loadProperties() {
		if (file != null && file.exists()) {
			InputStream in = null;
			try {
				in = new FileInputStream(file);
				properties.load(in);
				if (logger.isInfoEnabled()) {
					logger.info("Load registry store file " + file + ", data: " + properties);
				}
			} catch (Throwable e) {
				logger.warn("Failed to load registry store file " + file, e);
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						logger.warn(e.getMessage(), e);
					}
				}
			}
		}
	}

	private class SaveProperties implements Runnable {
		private long version;

		private SaveProperties(long version) {
			this.version = version;
		}

		public void run() {
			doSaveProperties(version);
		}
	}

	public void doSaveProperties(long version) {
		if (version < lastCacheChangedtime.get()) {
			return;
		}
		if (file == null) {
			return;
		}
		Properties newProperties = new Properties();

		InputStream in = null;
		try {
			if (file.exists()) {
				in = new FileInputStream(file);
				newProperties.load(in);
			}
		} catch (Throwable e) {
			logger.warn("Failed to load registry store file, cause: " + e.getMessage(), e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
		try {
			newProperties.putAll(properties);
			File lockfile = new File(file.getAbsolutePath() + ".lock");
			if (!lockfile.exists()) {
				lockfile.createNewFile();
			}
			RandomAccessFile raf = new RandomAccessFile(lockfile, "rw");
			try {
				FileChannel channel = raf.getChannel();
				try {
					FileLock lock = channel.tryLock();
					if (lock == null) {
						throw new IOException("Can not lock the registry cache file " + file.getAbsolutePath()
								+ ", ignore and retry later, maybe multi java process use the file, please config: dubbo.registry.file=xxx.properties");
					}
					try {
						if (!file.exists()) {
							file.createNewFile();
						}
						FileOutputStream outputFile = new FileOutputStream(file);
						try {
							newProperties.store(outputFile, "Dubbo Registry Cache");
						} finally {
							outputFile.close();
						}
					} finally {
						lock.release();
					}
				} finally {
					channel.close();
				}
			} finally {
				raf.close();
			}
		} catch (Throwable e) {
			if (version < lastCacheChangedtime.get()) {
				return;
			} else {
				registryCacheExecutor.execute(new SaveProperties(lastCacheChangedtime.incrementAndGet()));
			}
			logger.warn("Failed to save registry store file, cause: " + e.getMessage(), e);
		}
	}

	public Node getRegistryNode() {
		return registryNode;
	}

	public void setRegistryNode(Node registryNode) {
		this.registryNode = registryNode;
	}
	
}
