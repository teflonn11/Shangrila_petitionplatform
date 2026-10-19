package com.example.shangrilapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({"com.example.shangrilapp"})
@EntityScan("com.example.shangrilapp.model")
@EnableJpaRepositories("com.example.shangrilapp.repository")
public class ShangriLaPpApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShangriLaPpApplication.class, args);
	}



}
