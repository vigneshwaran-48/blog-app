package com.vicky.blog.staticservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.vicky.blog", "com.vicky.blog.staticservice" })
public class StaticServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(StaticServiceApplication.class, args);
    }
}
