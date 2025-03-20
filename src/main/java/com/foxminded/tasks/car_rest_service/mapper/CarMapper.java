package com.foxminded.tasks.car_rest_service.mapper;

import java.time.Year;

import org.springframework.stereotype.Component;

import com.foxminded.tasks.car_rest_service.dto.car.CarDTO;
import com.foxminded.tasks.car_rest_service.entity.Car;
import com.foxminded.tasks.car_rest_service.entity.Category;
import com.foxminded.tasks.car_rest_service.entity.Make;
import com.foxminded.tasks.car_rest_service.entity.Model;

@Component
public class CarMapper {

	public CarDTO carToCarDto(Car car) {
		
		return new CarDTO(
				car.getId(),
				car.getMake().getName(),
				car.getModel().getName(),
				car.getCategory().getName(),
				car.getYear().getValue(),
				car.getObjectId());
	}
	
	public Car carDtoToCar(CarDTO carDto, Make make, Model model, Category category) {

		return new Car(
				carDto.getId(),
				make,
				model,
				category,
				Year.of(carDto.getYear()),
				carDto.getObjectId());
	}
}
