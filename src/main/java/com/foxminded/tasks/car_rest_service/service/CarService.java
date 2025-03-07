package com.foxminded.tasks.car_rest_service.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.foxminded.tasks.car_rest_service.entity.Car;
import com.foxminded.tasks.car_rest_service.repository.CarRepository;
import com.foxminded.tasks.car_rest_service.specification.CarSpecification;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CarService {

	private final CarRepository carRepository;
	Logger logger = LoggerFactory.getLogger(CarService.class);

	@Autowired
	public CarService(CarRepository carRepository) {
		this.carRepository = carRepository;
	}

	public List<Car> findAll() {
		return carRepository.findAll();
	}

	public Car findById(Long id) {

		try {
			Optional<Car> optCar = carRepository.findById(id);

			if (optCar.isPresent()) {
				return optCar.get();

			} else {
				throw new EntityNotFoundException();
			}
		} catch (Exception e) {
			logger.error("Car with id {} is not found.", id);
			return new Car();
		}
	}

	public void save(Car car) {

		try {

			if (!isCarValid(car)) {
				throw new IllegalArgumentException();

			} else {
				carRepository.save(car);
			}

		} catch (Exception e) {
			logger.error("Save error. Car is not valid.");

			if (car != null) {
				logger.error("Make: {}", car.getMake());
				logger.error("Model: {}", car.getModel());
				logger.error("Category: {}", car.getCategory());
				logger.error("Year: {}", car.getYear());
				logger.error("ObjectId: {}", car.getObjectId());
			}
		}

	}

	public void delete(Car car) {

		try {

			if (!isCarValid(car)) {
				throw new IllegalArgumentException();

			} else {
				carRepository.delete(car);
			}
			
		} catch (Exception e) {
			logger.error("Delete error. Car is not valid.");
			if (car != null) {
				logger.error("ID: {}", car.getId());
				logger.error("Make: {}", car.getMake());
				logger.error("Model: {}", car.getModel());
				logger.error("Category: {}", car.getCategory());
				logger.error("Year: {}", car.getYear());
				logger.error("ObjectId: {}", car.getObjectId());
			}
		}
	}
	
	public Page<Car> filterCars(String makeName,
								String modelName,
								String categoryName,
								Integer year,
								Pageable pageable) {
		
		if(makeName == null || makeName.isEmpty()) {
			makeName = null;
		}
		if(modelName == null || modelName.isEmpty()) {
			modelName = null;
		}
		if(categoryName == null || categoryName.isEmpty()) {
			categoryName = null;
		}
		
		Specification<Car> specification = Specification.where(CarSpecification.filterByMake(makeName))
															.and(CarSpecification.filterByModel(modelName))
															.and(CarSpecification.filterByCategory(categoryName))
															.and(CarSpecification.filterByYear(year));
		
		return carRepository.findAll(specification, pageable);
	}

	private boolean isCarValid(Car car) {
		return car != null &&  
			   car.getMake() != null && 
			   car.getModel() != null&& 
			   car.getCategory() != null && 
			   car.getYear() != null && 
			   car.getObjectId() != null;
	}

}
