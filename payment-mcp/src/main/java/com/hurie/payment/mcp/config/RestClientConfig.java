package com.hurie.payment.mcp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    RestClient paymentRestClient() {

        return RestClient.builder()
                .baseUrl("http://localhost:8080")
                .build();
    }

}
