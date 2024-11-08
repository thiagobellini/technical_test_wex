package com.example.technical_test_wex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TechnicalTestWexApplication {

	public static void main(String[] args) {
		SpringApplication.run(TechnicalTestWexApplication.class, args);
	}

}
