package com.foxminded.tasks.car_rest_service.dto.car;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCarDTO {

	private String make;
	private String model;
	private String category;
	private int year;
	private String objectId;
}
