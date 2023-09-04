package com.likelion.catdogpia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CatdogpiaApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatdogpiaApplication.class, args);
	}

}
