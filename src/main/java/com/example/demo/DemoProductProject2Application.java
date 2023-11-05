package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = "com.example.demo")
public class DemoProductProject2Application {

	public static void main(String[] args) {
		SpringApplication.run(DemoProductProject2Application.class, args);
	}

}
