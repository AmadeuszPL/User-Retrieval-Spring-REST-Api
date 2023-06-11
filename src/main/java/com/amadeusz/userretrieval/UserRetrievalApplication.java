package com.amadeusz.userretrieval;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class UserRetrievalApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserRetrievalApplication.class, args);
	}

}
