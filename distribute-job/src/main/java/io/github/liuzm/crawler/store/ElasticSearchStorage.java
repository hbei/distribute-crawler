package io.github.liuzm.crawler.store;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.liuzm.crawler.jobconf.StoreConfig;
import io.github.liuzm.crawler.page.ExtractedPage;
import io.github.liuzm.crawler.store.StoreResult.Status;

public class ElasticSearchStorage extends Storage {

	private static final Logger log = LoggerFactory.getLogger(ElasticSearchStorage.class);

	private StoreConfig config;

	private TransportClient client;

	private String clusterNodes = "127.0.0.1:9300";
	private String clusterName = "elasticsearch";
	private Boolean clientTransportSniff = true;
	private Boolean clientIgnoreClusterName = Boolean.FALSE;
	private String clientPingTimeout = "5s";
	private String clientNodesSamplerInterval = "5s";
	static final String COLON = ":";
	static final String COMMA = ",";

	public ElasticSearchStorage(StoreConfig config) {
		super();
		this.config = config;
		if (config.getElasticsearchConfig().getUrl() != null) {
			clusterNodes = config.getElasticsearchConfig().getUrl();
		}
		if (config.getElasticsearchConfig().getClusterName() != null) {
			clusterName = config.getElasticsearchConfig().getClusterName();
		}
		beforeStore();
	}

	@Override
	public StoreResult beforeStore() {
		try {
			client = TransportClient.builder().settings(settings()).build();
			for (String clusterNode : clusterNodes.split(COMMA)) {
				String hostName = clusterNode.split(COLON)[0];
				String port = clusterNode.split(COLON)[1];
				log.info("adding transport node : " + clusterNode);
				client.addTransportAddress(
						new InetSocketTransportAddress(InetAddress.getByName(hostName), Integer.valueOf(port)));
			}
			client.connectedNodes();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public StoreResult onStore(ExtractedPage page) {
		StoreResult res = null;
		try {
			res = new StoreResult();

			if (null == page || page.getResult() == null) {
				res.setStatus(Status.ignored);
				return res;
			}
			
			Map<String, Object> content = (Map<String, Object>)page.getMessages().get("youku");
			log.info(content.toString());
			GetResponse get = client.prepareGet(config.getIndexName(), config.getElasticsearchConfig().getIndexType(), config.genId(content)).execute()
					.actionGet();

			if (null != get && get.isExists()) {
				index(config.indexName, config.getElasticsearchConfig().getIndexType(), config.genId(content), content);
			} else {
				index(config.indexName, config.getElasticsearchConfig().getIndexType(), config.genId(content), content);
			}
			content.clear();
			res.setStatus(Status.success);
			return res;

		} catch (Exception e) {

		}
		return null;
	}

	public void index(String index, String type, String id, Map<String, Object> data) {
		try {
			XContentBuilder xBuilder = jsonBuilder().startObject();
			Set<Entry<String, Object>> sets = data.entrySet();
			for (Entry<String, Object> entry : sets) {
				xBuilder.field(entry.getKey()).value(entry.getValue());
			}
			xBuilder.endObject();
			client.prepareIndex(index, type).setId(id).setSource(xBuilder).execute().get();
		} catch (ElasticsearchException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	@Override
	public StoreResult onStore(List<ExtractedPage> page) {
		System.out.println("store one page >>> " + page);
		return null;
	}

	@Override
	public StoreResult afterStore(ExtractedPage page) {

		client.close();
		return null;
	}

	private Settings settings() {
		return Settings.builder().put("cluster.name", clusterName).put("client.transport.sniff", clientTransportSniff)
				.put("client.transport.ignore_cluster_name", clientIgnoreClusterName)
				.put("client.transport.ping_timeout", clientPingTimeout)
				.put("client.transport.nodes_sampler_interval", clientNodesSamplerInterval).build();
	}

	/**
	 * @return the clusterNodes
	 */
	public String getClusterNodes() {
		return clusterNodes;
	}

	/**
	 * @param clusterNodes
	 *            the clusterNodes to set
	 */
	public void setClusterNodes(String clusterNodes) {
		this.clusterNodes = clusterNodes;
	}

	/**
	 * @return the clusterName
	 */
	public String getClusterName() {
		return clusterName;
	}

	/**
	 * @param clusterName
	 *            the clusterName to set
	 */
	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	/**
	 * @return the clientTransportSniff
	 */
	public Boolean getClientTransportSniff() {
		return clientTransportSniff;
	}

	/**
	 * @param clientTransportSniff
	 *            the clientTransportSniff to set
	 */
	public void setClientTransportSniff(Boolean clientTransportSniff) {
		this.clientTransportSniff = clientTransportSniff;
	}

	/**
	 * @return the clientIgnoreClusterName
	 */
	public Boolean getClientIgnoreClusterName() {
		return clientIgnoreClusterName;
	}

	/**
	 * @param clientIgnoreClusterName
	 *            the clientIgnoreClusterName to set
	 */
	public void setClientIgnoreClusterName(Boolean clientIgnoreClusterName) {
		this.clientIgnoreClusterName = clientIgnoreClusterName;
	}

	/**
	 * @return the clientPingTimeout
	 */
	public String getClientPingTimeout() {
		return clientPingTimeout;
	}

	/**
	 * @param clientPingTimeout
	 *            the clientPingTimeout to set
	 */
	public void setClientPingTimeout(String clientPingTimeout) {
		this.clientPingTimeout = clientPingTimeout;
	}

	/**
	 * @return the clientNodesSamplerInterval
	 */
	public String getClientNodesSamplerInterval() {
		return clientNodesSamplerInterval;
	}

	/**
	 * @param clientNodesSamplerInterval
	 *            the clientNodesSamplerInterval to set
	 */
	public void setClientNodesSamplerInterval(String clientNodesSamplerInterval) {
		this.clientNodesSamplerInterval = clientNodesSamplerInterval;
	}

}
