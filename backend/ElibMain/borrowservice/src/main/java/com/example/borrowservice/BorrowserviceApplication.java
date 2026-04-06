package com.example.borrowservice;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDiscoveryClient
public class BorrowserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BorrowserviceApplication.class, args);
	}

}
