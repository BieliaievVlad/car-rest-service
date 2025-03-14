package com.foxminded.tasks.car_rest_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarDTO {
	
	private Long id;
	private String make;
	private String model;
	private String category;
	private int year;
	private String objectId;
}
