package com.learnkafka.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.learnkafka"})
public class LibraryEventProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryEventProducerApplication.class, args);
	}

}
