package com.example.apigateway;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class ApigatewayApplication {

	public static void main(String[] args) {
        log.info("Starting API Gateway Application");
		SpringApplication.run(ApigatewayApplication.class, args);
	}

}
