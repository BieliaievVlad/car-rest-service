package com.foxminded.tasks.car_rest_service.service;

import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxminded.tasks.car_rest_service.dto.CarDTO;
import com.foxminded.tasks.car_rest_service.dto.CategoryDTO;
import com.foxminded.tasks.car_rest_service.dto.MakeDTO;
import com.foxminded.tasks.car_rest_service.dto.ModelDTO;
import com.foxminded.tasks.car_rest_service.entity.Car;
import com.foxminded.tasks.car_rest_service.entity.Category;
import com.foxminded.tasks.car_rest_service.entity.Make;
import com.foxminded.tasks.car_rest_service.entity.Model;
import com.foxminded.tasks.car_rest_service.mapper.CarMapper;
import com.foxminded.tasks.car_rest_service.mapper.CategoryMapper;
import com.foxminded.tasks.car_rest_service.mapper.MakeMapper;
import com.foxminded.tasks.car_rest_service.mapper.ModelMapper;

@Service
public class DataManagementService {
	
	private CarService carService;
	private MakeService makeService;
	private ModelService modelService;
	private CategoryService categoryService;
	private CarMapper carMapper;
	private final MakeMapper makeMapper;
	private final ModelMapper modelMapper;
	private final CategoryMapper categoryMapper;
	Logger logger = LoggerFactory.getLogger(DataManagementService.class);
	
	@Autowired
	public DataManagementService(CarService carService,
							  MakeService makeService,
							  ModelService modelService,
							  CategoryService categoryService,
							  CarMapper carMapper,
							  MakeMapper makeMapper,
							  ModelMapper modelMapper,
							  CategoryMapper categoryMapper) {
		this.carService = carService;
		this.makeService = makeService;
		this.modelService = modelService;
		this.categoryService = categoryService;
		this.carMapper = carMapper;
		this.makeMapper = makeMapper;
		this.modelMapper = modelMapper;
		this.categoryMapper = categoryMapper;
	}
	
	public List<CarDTO> findAllCars() {
		
		return carService.findAll().stream()
				.map(carMapper :: carToDto)
				.collect(Collectors.toList());
	}
	
	public List<MakeDTO> findAllMakes() {
		
		return makeService.findAll().stream()
				.map(makeMapper :: makeToDto)
				.collect(Collectors.toList());
	}
	
	public List<ModelDTO> findAllModels() {
		
		return modelService.findAll().stream()
				.map(modelMapper :: modelToDto)
				.collect(Collectors.toList());
	}
	
	public List<CategoryDTO> findAllCategories() {
		return categoryService.findAll().stream()
				.map(categoryMapper :: categoryToDto)
				.collect(Collectors.toList());
	}
	
	public CarDTO findCarById(Long id) {
		
		Car car = carService.findById(id);
		
		return carMapper.carToDto(car);
	}
	
	public MakeDTO findMakeById(Long id) {
		
		Make make = makeService.findById(id);
		
		return makeMapper.makeToDto(make);
	}
	
	public ModelDTO findModelById(Long id) {
		
		Model model = modelService.findById(id);
		
		return modelMapper.modelToDto(model);
	}
	
	public CategoryDTO findCategoryById(Long id) {
		
		Category category = categoryService.findById(id);
		
		return categoryMapper.categoryToDto(category);
	}
	
	public void deleteCarById(Long id) {
		
		Car car = carService.findById(id);

		carService.delete(car);
	}
	
	public Car createCar(CarDTO carDto) {

		Make make = makeService.findByNameOrSaveNew(carDto.getMake());
		Model model = modelService.findByNameOrSaveNew(carDto.getModel());
		Category category = categoryService.findByNameOrSaveNew(carDto.getCategory());
		Year year = Year.of(carDto.getYear());
		String objectId = carDto.getObjectId();
		
		if(objectId == null || objectId.isEmpty()) {
			objectId = carService.generateObjectId();
		}
		
		return carService.save(new Car(make, model, category, year, objectId));		
	}
	
	public Make createMake(MakeDTO makeDto) {
		
		if(!makeService.existsByName(makeDto.getName())) {

			return makeService.save(new Make(makeDto.getName()));
			
		} else {
			logger.error("Make with name {} is already exists.", makeDto.getName());
			throw new IllegalArgumentException();
		}	
	}
	public Model createModel(ModelDTO modelDto) {
		
		if(!modelService.existsByName(modelDto.getName())) {
			
			return modelService.save(new Model(modelDto.getName()));
			
		} else {
			logger.error("Model with name {} is already exists.", modelDto.getName());
			throw new IllegalArgumentException();
		}	
	}
	public Category createCategory(CategoryDTO categoryDto) {
		
		if(!categoryService.existsByName(categoryDto.getName())) {
			
			return categoryService.save(new Category(categoryDto.getName()));
			
		} else {
			logger.error("Category with name {} is already exists.", categoryDto.getName());
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
	
	public Car dtoToCar(CarDTO carDto) {
		
		Make make = makeService.findByNameOrSaveNew(carDto.getMake());
		Model model = modelService.findByNameOrSaveNew(carDto.getModel());
		Category category = categoryService.findByNameOrSaveNew(carDto.getCategory());
		
		return carMapper.dtoToCar(carDto, make, model, category);
	}

}
