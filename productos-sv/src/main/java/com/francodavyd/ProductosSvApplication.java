package com.francodavyd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ProductosSvApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductosSvApplication.class, args);
	}

}
