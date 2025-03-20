//package com.foxminded.tasks.car_rest_service.service;
//
//import java.time.Year;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.foxminded.tasks.car_rest_service.dto.car.CarDTO;
//import com.foxminded.tasks.car_rest_service.entity.Car;
//import com.foxminded.tasks.car_rest_service.entity.Category;
//import com.foxminded.tasks.car_rest_service.entity.Make;
//import com.foxminded.tasks.car_rest_service.entity.Model;
//import com.foxminded.tasks.car_rest_service.mapper.CarMapper;
//import com.foxminded.tasks.car_rest_service.mapper.CategoryMapper;
//import com.foxminded.tasks.car_rest_service.mapper.MakeMapper;
//import com.foxminded.tasks.car_rest_service.mapper.ModelMapper;
//
//@Service
//public class DataManagementService {
//	
//	private CarService carService;
//	private MakeService makeService;
//	private ModelService modelService;
//	private CategoryService categoryService;
//	private CarMapper carMapper;
//	Logger logger = LoggerFactory.getLogger(DataManagementService.class);
//	
//	@Autowired
//	public DataManagementService(CarService carService,
//							  MakeService makeService,
//							  ModelService modelService,
//							  CategoryService categoryService,
//							  CarMapper carMapper,
//							  MakeMapper makeMapper,
//							  ModelMapper modelMapper,
//							  CategoryMapper categoryMapper) {
//		this.carService = carService;
//		this.makeService = makeService;
//		this.modelService = modelService;
//		this.categoryService = categoryService;
//		this.carMapper = carMapper;
//		this.makeMapper = makeMapper;
//		this.modelMapper = modelMapper;
//		this.categoryMapper = categoryMapper;
//	}
//	
//	public CarDTO findCarById(Long id) {
//		
//		Car car = carService.findById(id);
//		
//		return carMapper.carToDto(car);
//	}
//
//	public void deleteCarById(Long id) {
//		
//		Car car = carService.findById(id);
//
//		carService.delete(car);
//	}
//	
//	public Car createCar(CarDTO carDto) {
//
//		Make make = makeService.findByNameOrSaveNew(carDto.getMake());
//		Model model = modelService.findByNameOrSaveNew(carDto.getModel());
//		Category category = categoryService.findByNameOrSaveNew(carDto.getCategory());
//		Year year = Year.of(carDto.getYear());
//		String objectId = carDto.getObjectId();
//		
//		if(objectId == null || objectId.isEmpty()) {
//			objectId = carService.generateObjectId();
//		}
//		
//		return carService.save(new Car(make, model, category, year, objectId));		
//	}
//	
//	public Car dtoToCar(CarDTO carDto) {
//		
//		Make make = makeService.findByNameOrSaveNew(carDto.getMake());
//		Model model = modelService.findByNameOrSaveNew(carDto.getModel());
//		Category category = categoryService.findByNameOrSaveNew(carDto.getCategory());
//		
//		return carMapper.dtoToCar(carDto, make, model, category);
//	}
//
//}
