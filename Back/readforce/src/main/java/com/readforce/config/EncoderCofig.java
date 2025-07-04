package com.readforce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class EncoderCofig {
	
	@Bean
	public PasswordEncoder password_encoder() {
		return new BCryptPasswordEncoder();
	}
	
}
