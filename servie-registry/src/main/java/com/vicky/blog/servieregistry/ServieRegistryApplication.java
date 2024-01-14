package com.vicky.blog.servieregistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ServieRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServieRegistryApplication.class, args);
	}

}
