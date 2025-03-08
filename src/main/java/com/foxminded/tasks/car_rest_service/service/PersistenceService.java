package com.foxminded.tasks.car_rest_service.service;

import java.time.Year;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxminded.tasks.car_rest_service.entity.Car;
import com.foxminded.tasks.car_rest_service.entity.Category;
import com.foxminded.tasks.car_rest_service.entity.Make;
import com.foxminded.tasks.car_rest_service.entity.Model;

@Service
public class PersistenceService {
	
	private CarService carService;
	private MakeService makeService;
	private ModelService modelService;
	private CategoryService categoryService;
	
	@Autowired
	public PersistenceService(CarService carService,
							  MakeService makeService,
							  ModelService modelService,
							  CategoryService categoryService) {
		this.carService = carService;
		this.makeService = makeService;
		this.modelService = modelService;
		this.categoryService = categoryService;
	}
	
	public void deleteCarById(Long id) {
		
		Car car = carService.findById(id);
		carService.delete(car);
	}
	
	public Car createCar(Car car) {

		Make make = makeService.findByNameOrSaveNew(car.getMake().getName());
		Model model = modelService.findByNameOrSaveNew(car.getModel().getName());
		Category category = categoryService.findByNameOrSaveNew(car.getCategory().getName());
		Year year = car.getYear();
		String objectId = car.getObjectId();
		
		if(objectId == null || objectId.isEmpty()) {
			objectId = carService.generateObjectId();
		}
		
		Car newCar = new Car(make, model, category, year, objectId);
		
		return carService.save(newCar);
				
	}

}
