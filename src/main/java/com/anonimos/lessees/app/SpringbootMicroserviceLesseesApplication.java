package com.anonimos.lessees.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
@EnableJpaAuditing
public class SpringbootMicroserviceLesseesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootMicroserviceLesseesApplication.class, args);
	}

}
