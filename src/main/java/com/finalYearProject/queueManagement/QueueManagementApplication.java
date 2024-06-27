package com.finalYearProject.queueManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

//@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@SpringBootApplication
@EnableScheduling
public class QueueManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(QueueManagementApplication.class, args);
	}

}
