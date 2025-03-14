package com.foxminded.tasks.car_rest_service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.foxminded.tasks.car_rest_service.dto.CarDTO;
import com.foxminded.tasks.car_rest_service.entity.Car;
import com.foxminded.tasks.car_rest_service.entity.Category;
import com.foxminded.tasks.car_rest_service.entity.Make;
import com.foxminded.tasks.car_rest_service.entity.Model;
import com.foxminded.tasks.car_rest_service.mapper.CarMapper;
import com.foxminded.tasks.car_rest_service.repository.CarRepository;
import com.foxminded.tasks.car_rest_service.specification.CarSpecification;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CarService {

	private final CarRepository carRepository;
	private final CarMapper mapper;
	Logger logger = LoggerFactory.getLogger(CarService.class);

	@Autowired
	public CarService(CarRepository carRepository, CarMapper carMapper) {
		this.carRepository = carRepository;
		this.mapper = carMapper;
	}

	public List<CarDTO> findAll() {
		
		List<Car> cars = carRepository.findAll();
		List<CarDTO> carsDto = new ArrayList<>();
		
		for(Car c : cars) {
			CarDTO carDto = mapper.carToDto(c);
			carsDto.add(carDto);
		}
		return carsDto;
	}

	public CarDTO findById(Long id) {

		Optional<Car> optCar = carRepository.findById(id);

		if (optCar.isPresent()) {
			return mapper.carToDto(optCar.get());

		} else {
			logger.error("Car with id {} is not found.", id);
			throw new EntityNotFoundException();

		}
	}

	public Car save(Car car) {

		if (!isCarValid(car)) {
			logger.error("Save error. Car is not valid.");
			throw new IllegalArgumentException();

		} else {
			return carRepository.save(car);
		}

	}

	public void delete(Car car) {

		if (!isCarValid(car)) {
			logger.error("Delete error. Car is not valid.");
			throw new IllegalArgumentException();

		} else {
			carRepository.delete(car);
		}

	}

	public Page<Car> filterCars(String makeName, String modelName, String categoryName, Integer year,
			Pageable pageable) {

		if (makeName == null || makeName.isEmpty()) {
			makeName = null;
		}
		if (modelName == null || modelName.isEmpty()) {
			modelName = null;
		}
		if (categoryName == null || categoryName.isEmpty()) {
			categoryName = null;
		}

		Specification<Car> specification = Specification.where(CarSpecification.filterByMake(makeName))
														.and(CarSpecification.filterByModel(modelName))
														.and(CarSpecification.filterByCategory(categoryName))
														.and(CarSpecification.filterByYear(year));

		return carRepository.findAll(specification, pageable);
	}
	
	public List<Car> findByMake(Make make) {
		return carRepository.findByMake(make);
	}
	
	public List<Car> findByModel(Model model) {
		return carRepository.findByModel(model);
	}
	
	public List<Car> findByCategory(Category category) {
		return carRepository.findByCategory(category);
	}
	
	public String generateObjectId() {
		
		String objectId;
		String customAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		char[] alphabet = customAlphabet.toCharArray();
		Random random = new Random();
		
        do {
            objectId = NanoIdUtils.randomNanoId(random, alphabet, 11);
            
        } while (carRepository.existsByObjectId(objectId));
        
        return objectId;
	}
	
	private boolean isCarValid(Car car) {
		return car != null && 
			   car.getMake() != null && 
			   car.getModel() != null && 
			   car.getCategory() != null && 
			   car.getYear() != null && 
			   car.getObjectId() != null;
	}
}
