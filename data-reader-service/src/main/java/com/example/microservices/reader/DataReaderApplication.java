package com.example.microservices.reader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
public class DataReaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataReaderApplication.class, args);
	}

}
