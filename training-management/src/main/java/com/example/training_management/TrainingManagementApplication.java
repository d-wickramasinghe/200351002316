package com.example.training_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.example")
@EntityScan("com.example.entity")
@EnableJpaRepositories("com.example.repository")
public class TrainingManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrainingManagementApplication.class, args);
	}

}
