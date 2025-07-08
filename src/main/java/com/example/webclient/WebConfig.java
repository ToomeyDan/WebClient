package com.example.webclient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class WebConfig {

    protected static final Logger logger = LogManager.getLogger();

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://example.org")
                .defaultCookie("cookie-name", "cookie-value")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .filters(exchangeFilterFunctions -> {
                    exchangeFilterFunctions.add(logRequest());
                    exchangeFilterFunctions.add(logResponse());
                })
                .build();
    }

    ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            if (logger.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder("Request: \n");
                //append clientRequest method and url
                clientRequest
                        .headers()
                        .forEach((name, values) -> values.forEach(value -> sb.append(name + ":" + value)));
                logger.debug(sb.toString());
            }
            return Mono.just(clientRequest);
        });
    }

    ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (logger.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder("Response: \n");
                //append clientRequest method and url
                // clientRequest
                //         .headers()
                //         .forEach((name, values) -> values.forEach(value -> /* append header key/value */));
                logger.debug(sb.toString());
            }
            return Mono.just(clientResponse);
        });
    }
}
