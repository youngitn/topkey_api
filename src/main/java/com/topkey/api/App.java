package com.topkey.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
public class App {
	@Value("${test.test1.test2}")
    private String test2;
	

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@PostConstruct
    public void showCustomConfiguration() {
        System.out.println("Custom test2: " + test2);

	}
}
