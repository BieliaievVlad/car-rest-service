package com.foxminded.tasks.car_rest_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foxminded.tasks.car_rest_service.entity.Car;
import com.foxminded.tasks.car_rest_service.service.CarService;
import com.foxminded.tasks.car_rest_service.service.PersistenceService;

@RestController
@RequestMapping("/api/v1")
public class CarController {

    private final CarService carService;
    private final PersistenceService service;

    @Autowired
    public CarController(CarService carService, PersistenceService service) {
        this.carService = carService;
        this.service = service;
    }

    @GetMapping("/cars")
    public Page<Car> getFilteredCars(
            @RequestParam(required = false) String makeName,
            @RequestParam(required = false) String modelName,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) Integer year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
    	Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
        return carService.filterCars(makeName, modelName, categoryName, year, pageable);
    }
    
    @GetMapping("/cars/{id}")
    public Car getCar(@PathVariable Long id) {
    	return carService.findById(id);
    }
    
	@PostMapping("/cars")
	public ResponseEntity<Car> createCar(@RequestBody Car car) {

		try {
			Car newCar = service.createCar(car);
			return new ResponseEntity<>(newCar, HttpStatus.CREATED);
			
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/cars/{id}")
	public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
		
		try {

			service.deleteCarById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
