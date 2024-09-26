package com.francodavyd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class NotificacionesSvApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificacionesSvApplication.class, args);
	}

}
