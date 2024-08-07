package com.keyvin.es.midware.es;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author weiwh
 * @date 2020/3/30 0:00
 */
@Configuration
public class ElasticSearchConfig {
    private static final Logger log = LoggerFactory.getLogger(ElasticSearchConfig.class);

    //es地址
    @Value("${elasticsearch.host}")
    private String hostName;
    @Value("${elasticsearch.port}")
    private Integer port;
    @Value("${elasticsearch.protocol}")
    private String protocol;
    @Value("${elasticsearch.clusterName}")
    private String clusterName;

    @Bean
    public RestHighLevelClient restHighLevelClient(){
        HttpHost httpHost = new HttpHost(hostName, port, "http");
        RestClientBuilder builder = RestClient.builder(httpHost);
        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;
    }

}
