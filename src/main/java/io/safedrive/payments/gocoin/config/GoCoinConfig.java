package io.safedrive.payments.gocoin.config;

import io.safedrive.payments.gocoin.service.impl.GoCoinServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoCoinConfig {
    
    @Bean
    public GoCoinServiceImpl goCoinService(
            @Value("${gocoin.api.key}") String apiKey,
            @Value("${gocoin.merchant.id}") String merchantId) {
        return new GoCoinServiceImpl(apiKey, merchantId);
    }
}
