package com.francodavyd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PagosSvApplication {

	public static void main(String[] args) {
		SpringApplication.run(PagosSvApplication.class, args);
	}

}
