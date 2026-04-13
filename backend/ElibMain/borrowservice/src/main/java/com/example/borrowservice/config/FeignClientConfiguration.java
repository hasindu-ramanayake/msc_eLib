package com.example.borrowservice.config;

import com.example.borrowservice.client.FeignErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfiguration {

    @Bean
    public ErrorDecoder FeignErrorDecoder(){
        return new FeignErrorDecoder();
    }
}
