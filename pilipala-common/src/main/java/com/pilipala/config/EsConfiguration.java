package com.pilipala.config;

import com.pilipala.config.AppConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

import javax.annotation.Resource;

@Configuration
public class EsConfiguration extends AbstractElasticsearchConfiguration implements DisposableBean {
    @Resource
    private AppConfig appConfig;

    private RestHighLevelClient client;

    @Override
    public void destroy() throws Exception {
        if (client != null) {
            client.close();
        }
    }

    @Override
    public RestHighLevelClient elasticsearchClient() {
        String host = appConfig.getHost();
        String port = appConfig.getEsHostPort();
        String hostAndPort = host + ":" + port;
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(hostAndPort)
                .build();
        client = RestClients.create(clientConfiguration).rest();
        return client;
    }
}
