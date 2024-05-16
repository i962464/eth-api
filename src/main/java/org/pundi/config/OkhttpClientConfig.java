package org.pundi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import okhttp3.OkHttpClient;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年05月16日 16:51:00
 */
@Configuration
public class OkhttpClientConfig {

  @Bean
  public OkHttpClient okHttpClient() {
    return new OkHttpClient();
  }
}
