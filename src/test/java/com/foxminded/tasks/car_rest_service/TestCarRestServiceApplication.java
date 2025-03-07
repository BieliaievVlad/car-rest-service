package com.foxminded.tasks.car_rest_service;

import org.springframework.boot.SpringApplication;

public class TestCarRestServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(CarRestServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
