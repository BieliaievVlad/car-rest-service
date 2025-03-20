package com.foxminded.tasks.car_rest_service.service;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.foxminded.tasks.car_rest_service.dto.car.CarDTO;
import com.foxminded.tasks.car_rest_service.dto.car.CarListItemDTO;
import com.foxminded.tasks.car_rest_service.dto.car.CreateCarDTO;
import com.foxminded.tasks.car_rest_service.dto.car.UpdateCarDTO;
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
	private MakeService makeService;
	private ModelService modelService;
	private CategoryService categoryService;
	private final CarMapper mapper;
	Logger logger = LoggerFactory.getLogger(CarService.class);

	@Autowired
	public CarService(CarRepository carRepository, 
					  MakeService makeService,
					  ModelService modelService,
					  CategoryService categoryService,
					  CarMapper mapper) {
		this.carRepository = carRepository;
		this.makeService = makeService;
		this.modelService = modelService;
		this.categoryService = categoryService;
		this.mapper = mapper;
	}

	public List<Car> findAll() {
		
		return carRepository.findAll();
	}

	public Car findById(Long id) {

		Optional<Car> optCar = carRepository.findById(id);

		if (optCar.isPresent()) {
			return optCar.get();

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
	
	public CarDTO findCarById(Long id) {
		
		Car car = findById(id);
		
		return mapper.carToCarDto(car);
	}
	
	public CarDTO createCar(CreateCarDTO createCarDto) {

		Make make = makeService.findByNameOrSaveNew(createCarDto.getMake());
		Model model = modelService.findByNameOrSaveNew(createCarDto.getModel());
		Category category = categoryService.findByNameOrSaveNew(createCarDto.getCategory());
		Year year = Year.of(createCarDto.getYear());
		String objectId = createCarDto.getObjectId();
		
		if(objectId == null || objectId.isEmpty()) {
			objectId = generateObjectId();
		}
		
		Car newCar = save(new Car(make, model, category, year, objectId));
		
		return mapper.carToCarDto(newCar);		
	}
	
	public CarDTO updateCar(Long id, UpdateCarDTO updateCarDto) {
		
		Car carToUpdate = findById(id);
		Make makeToUpdate = carToUpdate.getMake();
		Model modelToUpdate = carToUpdate.getModel();
		Category categoryToUpdate = carToUpdate.getCategory();
		
		makeToUpdate.setName(updateCarDto.getMake());
		modelToUpdate.setName(updateCarDto.getModel());
		categoryToUpdate.setName(updateCarDto.getCategory());
		Year updatedYear = Year.of(updateCarDto.getYear());
		
		carToUpdate.setMake(makeToUpdate);
		carToUpdate.setModel(modelToUpdate);
		carToUpdate.setCategory(categoryToUpdate);
		carToUpdate.setYear(updatedYear);
		
		Car updatedCar = save(carToUpdate);
		
		return mapper.carToCarDto(updatedCar);
	}
	
	public void deleteCarById(Long id) {
		
		Car car = findById(id);

		delete(car);
	}

	public Page<CarListItemDTO> filterCars(String makeName, String modelName, String categoryName, Integer year,
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
		Page<Car> carsPage = carRepository.findAll(specification, pageable);
		
		List<CarListItemDTO> carsListDto = carsPage.getContent().stream()
				.map(mapper :: carToCarListItemDto)
				.collect(Collectors.toList());
		
		return new PageImpl<>(carsListDto, pageable, carsPage.getTotalElements());
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
	
	public void deleteMakeAndAssociations(Long id) {
		
		Make make = makeService.findById(id);
		List<Car> cars = findByMake(make);
		
		for (Car c : cars) {
			delete(c);
		}
		
		makeService.delete(make);
	}
	
	public void deleteModelAndAssociations(Long id) {
		
		Model model = modelService.findById(id);
		List<Car> cars = findByModel(model);
		
		for (Car c : cars) {
			delete(c);
		}
		
		modelService.delete(model);
	}
	
	public void deleteCategoryAndAssociations(Long id) {
		
		Category category = categoryService.findById(id);
		List<Car> cars = findByCategory(category);
		
		for (Car c : cars) {
			delete(c);
		}
		
		categoryService.delete(category);
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
