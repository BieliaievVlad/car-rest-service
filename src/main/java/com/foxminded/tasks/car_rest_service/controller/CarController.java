package com.foxminded.tasks.car_rest_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foxminded.tasks.car_rest_service.entity.Car;
import com.foxminded.tasks.car_rest_service.service.CarService;

@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
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
}
