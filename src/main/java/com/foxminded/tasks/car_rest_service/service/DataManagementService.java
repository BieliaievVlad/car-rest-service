package com.foxminded.tasks.car_rest_service.service;

import java.time.Year;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxminded.tasks.car_rest_service.entity.Car;
import com.foxminded.tasks.car_rest_service.entity.Category;
import com.foxminded.tasks.car_rest_service.entity.Make;
import com.foxminded.tasks.car_rest_service.entity.Model;

@Service
public class DataManagementService {
	
	private CarService carService;
	private MakeService makeService;
	private ModelService modelService;
	private CategoryService categoryService;
	Logger logger = LoggerFactory.getLogger(DataManagementService.class);
	
	@Autowired
	public DataManagementService(CarService carService,
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
	
	public Make createMake(Make make) {
		
		if(!makeService.existsByName(make.getName())) {
			
			return makeService.save(new Make(make.getName()));
			
		} else {
			logger.error("Make with name {} is already exists.", make.getName());
			throw new IllegalArgumentException();
		}	
	}
	public Model createModel(Model model) {
		
		if(!modelService.existsByName(model.getName())) {
			
			return modelService.save(new Model(model.getName()));
			
		} else {
			logger.error("Model with name {} is already exists.", model.getName());
			throw new IllegalArgumentException();
		}	
	}
	public Category createCategory(Category category) {
		
		if(!categoryService.existsByName(category.getName())) {
			
			return categoryService.save(new Category(category.getName()));
			
		} else {
			logger.error("Category with name {} is already exists.", category.getName());
			throw new IllegalArgumentException();
		}	
	}
	
	public void deleteMakeAndAssociations(Long id) {
		
		Make make = makeService.findById(id);
		List<Car> cars = carService.findByMake(make);
		
		for (Car c : cars) {
			carService.delete(c);
		}
		
		makeService.delete(make);
	}
	public void deleteModelAndAssociations(Long id) {
		
		Model model = modelService.findById(id);
		List<Car> cars = carService.findByModel(model);
		
		for (Car c : cars) {
			carService.delete(c);
		}
		
		modelService.delete(model);
	}
	public void deleteCategoryAndAssociations(Long id) {
		
		Category category = categoryService.findById(id);
		List<Car> cars = carService.findByCategory(category);
		
		for (Car c : cars) {
			carService.delete(c);
		}
		
		categoryService.delete(category);
	}

}
