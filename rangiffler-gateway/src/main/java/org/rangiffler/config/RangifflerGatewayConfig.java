package org.rangiffler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class RangifflerGatewayConfig {

  public static final int MAX_PHOTO_SIZE = Integer.MAX_VALUE;

  @Bean
  public WebClient webClient() {
    return WebClient
        .builder()
        .exchangeStrategies(ExchangeStrategies.builder().codecs(
            configurer -> configurer.defaultCodecs().maxInMemorySize(MAX_PHOTO_SIZE)).build())
        .build();
  }
}
