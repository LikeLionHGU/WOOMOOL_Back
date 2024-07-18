package com.project.woomool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WoomoolApplication {

	public static void main(String[] args) {
		SpringApplication.run(WoomoolApplication.class, args);
	}

}
