package com.francodavyd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PedidosSvApplication {

	public static void main(String[] args) {
		SpringApplication.run(PedidosSvApplication.class, args);
	}

}
