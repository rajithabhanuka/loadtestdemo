package com.java.loadtestdemo.configs;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestConfigFactory {

    @Autowired
    private HttpHostsConfiguration httpHostConfiguration;

    @Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager result = new PoolingHttpClientConnectionManager();
        result.setMaxTotal(this.httpHostConfiguration.getMaxTotal());
        result.setDefaultMaxPerRoute(this.httpHostConfiguration.getDefaultMaxPerRoute());

        if (CollectionUtils.isNotEmpty(this.httpHostConfiguration.getMaxPerRoutes())) {
            for (HttpHostsConfiguration.HttpHostConfiguration httpHostConfig : this.httpHostConfiguration.getMaxPerRoutes()) {
                HttpHost host = new HttpHost(httpHostConfig.getHost(), httpHostConfig.getPort(), httpHostConfig.getScheme());
                result.setMaxPerRoute(new HttpRoute(host), httpHostConfig.getMaxPerRoute());
            }
        }

        return result;
    }

    @Bean
    public RequestConfig requestConfig() {
        RequestConfig result = RequestConfig.custom()
                .setConnectionRequestTimeout(2000)
                .setConnectTimeout(2000)
                .setSocketTimeout(2000)
                .build();
        return result;
    }

    @Bean
    public CloseableHttpClient httpClient(PoolingHttpClientConnectionManager poolingHttpClientConnectionManager, RequestConfig requestConfig) {
        CloseableHttpClient result = HttpClientBuilder
                .create()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
        return result;
    }

    @Bean
    public RestTemplate restTemplate(HttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        return new RestTemplate(requestFactory);
    }


}
