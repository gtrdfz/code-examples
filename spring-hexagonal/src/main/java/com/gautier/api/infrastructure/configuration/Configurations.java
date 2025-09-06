package com.gautier.api.infrastructure.configuration;


import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Configurations {

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }
}
