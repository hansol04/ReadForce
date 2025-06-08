package com.readforce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ReadforceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReadforceApplication.class, args);
	}

}
