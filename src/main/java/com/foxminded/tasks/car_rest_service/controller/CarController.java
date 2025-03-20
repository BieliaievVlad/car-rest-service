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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foxminded.tasks.car_rest_service.dto.car.CarDTO;
import com.foxminded.tasks.car_rest_service.dto.car.CarListItemDTO;
import com.foxminded.tasks.car_rest_service.dto.car.CreateCarDTO;
import com.foxminded.tasks.car_rest_service.dto.car.UpdateCarDTO;
import com.foxminded.tasks.car_rest_service.service.CarService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/v1")
public class CarController {

    private final CarService service;

    @Autowired
    public CarController(CarService service) {
        this.service = service;
    }

    @GetMapping("/cars")
    public Page<CarListItemDTO> getFilteredCars(
            @RequestParam(required = false) String makeName,
            @RequestParam(required = false) String modelName,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) Integer year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
    	Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
        return service.filterCars(makeName, modelName, categoryName, year, pageable);
    }
    
    @GetMapping("/cars/{id}")
    public ResponseEntity<CarDTO> getCar(@PathVariable Long id) {
    	
    	try {
    		CarDTO carDto = service.findCarById(id);
    		return new ResponseEntity<>(carDto, HttpStatus.OK);
    		
    	} catch (EntityNotFoundException e){	
    		return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
    		
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
    
	@PostMapping("/cars")
	public ResponseEntity<CarDTO> createCar(@RequestBody CreateCarDTO createCarDto) {

		try {
			CarDTO carDto = service.createCar(createCarDto);
			return new ResponseEntity<>(carDto, HttpStatus.CREATED);
			
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/cars/{id}")
	public ResponseEntity<CarDTO> updateCar(@PathVariable Long id, @RequestBody UpdateCarDTO updateCarDto) {
		
		try {
			CarDTO carDto = service.updateCar(id, updateCarDto);
			return new ResponseEntity<>(carDto, HttpStatus.OK);
			
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/cars/{id}")
	public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
		
		try {

			service.deleteCarById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
