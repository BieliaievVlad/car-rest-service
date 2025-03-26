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
import com.foxminded.tasks.car_rest_service.dto.category.CategoryDTO;
import com.foxminded.tasks.car_rest_service.dto.make.MakeDTO;
import com.foxminded.tasks.car_rest_service.dto.model.ModelDTO;
import com.foxminded.tasks.car_rest_service.entity.Car;
import com.foxminded.tasks.car_rest_service.entity.Category;
import com.foxminded.tasks.car_rest_service.entity.Make;
import com.foxminded.tasks.car_rest_service.entity.Model;
import com.foxminded.tasks.car_rest_service.mapper.CarMapper;
import com.foxminded.tasks.car_rest_service.mapper.CategoryMapper;
import com.foxminded.tasks.car_rest_service.mapper.MakeMapper;
import com.foxminded.tasks.car_rest_service.mapper.ModelMapper;
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
	private final MakeMapper makeMapper;
	private final ModelMapper modelMapper;
	private final CategoryMapper categoryMapper;
	Logger logger = LoggerFactory.getLogger(CarService.class);

	@Autowired
	public CarService(CarRepository carRepository, 
					  MakeService makeService,
					  ModelService modelService,
					  CategoryService categoryService,
					  CarMapper mapper,
					  MakeMapper makeMapper,
					  ModelMapper modelMapper,
					  CategoryMapper categoryMapper) {
		this.carRepository = carRepository;
		this.makeService = makeService;
		this.modelService = modelService;
		this.categoryService = categoryService;
		this.mapper = mapper;
		this.makeMapper = makeMapper;
		this.modelMapper = modelMapper;
		this.categoryMapper = categoryMapper;
	}
	
	public CarDTO findCarById(Long id) {
		
		Optional<Car> optCar = carRepository.findById(id);
		
		if(!optCar.isEmpty()) {
			
			return mapper.carToCarDto(optCar.get());
			
		} else {
			logger.error("Car with id {} is not found.", id);
			throw new EntityNotFoundException();
		}
		
	}
	
	public CarDTO createCar(CreateCarDTO createCarDto) {

		MakeDTO makeDto = makeService.findByNameOrSaveNew(createCarDto.getMake());
		Make make = makeMapper.dtoToMake(makeDto);
		ModelDTO modelDto = modelService.findByNameOrSaveNew(createCarDto.getModel());
		Model model = modelMapper.dtoToModel(modelDto);
		CategoryDTO categoryDto = categoryService.findByNameOrSaveNew(createCarDto.getCategory());
		Category category = categoryMapper.dtoToCategory(categoryDto);
		Year year = Year.of(createCarDto.getYear());
		String objectId = createCarDto.getObjectId();
		
		if(objectId == null || objectId.isEmpty()) {
			objectId = generateObjectId();
		}
		
		Car newCar = carRepository.save(new Car(make, model, category, year, objectId));
		
		return mapper.carToCarDto(newCar);		
	}
	
	public CarDTO updateCar(Long id, UpdateCarDTO updateCarDto) {
		
		Optional<Car> optCarToUpdate = carRepository.findById(id);
		
		if(!optCarToUpdate.isEmpty()) {
			
			Car carToUpdate = optCarToUpdate.get();
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
			
			Car updatedCar = carRepository.save(carToUpdate);
			
			return mapper.carToCarDto(updatedCar);
			
		} else {
			logger.error("Car with id {} is not found.", id);
			throw new EntityNotFoundException();
		}

	}
	
	public void delete(Long id) {
		
		Optional<Car> optCar = carRepository.findById(id);
		
		if(!optCar.isEmpty()) {
			carRepository.delete(optCar.get());
			
		} else {
			logger.error("Car with id {} is not found.", id);
			throw new EntityNotFoundException();
		}	
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
	
	public void deleteMakeAndAssociations(Long id) {

		List<Car> cars = carRepository.findByMake_Id(id);
		
		for (Car c : cars) {
			carRepository.delete(c);
		}
		
		makeService.delete(id);
	}
	
	public void deleteModelAndAssociations(Long id) {

		List<Car> cars = carRepository.findByModel_Id(id);
		
		for (Car c : cars) {
			carRepository.delete(c);
		}
		
		modelService.delete(id);
	}
	
	public void deleteCategoryAndAssociations(Long id) {

		List<Car> cars = carRepository.findByCategory_Id(id);
		
		for (Car c : cars) {
			carRepository.delete(c);
		}
		
		categoryService.delete(id);
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
}
