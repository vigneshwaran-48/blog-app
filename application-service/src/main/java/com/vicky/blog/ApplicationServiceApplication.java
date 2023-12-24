package com.vicky.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ApplicationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationServiceApplication.class, args);
	}

}
