package com.foxminded.tasks.car_rest_service.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

	@InjectMocks
	CarService carService;

	@Mock
	CarRepository carRepository;
	
	@Mock
	MakeService makeService;
	
	@Mock
	ModelService modelService;
	
	@Mock
	CategoryService categoryService;
	
	@Mock
	CarMapper mapper;
	
	@Mock
	MakeMapper makeMapper;
	
	@Mock
	ModelMapper modelMapper;
	
	@Mock
	CategoryMapper categoryMapper;

	@Test
	void findcarById_ValidId_CalledMethodsAndReturnsExpected() {
		
		Long id = 1L;
		Make make = new Make(1L, "Make_Name");
		Model model = new Model(1L, "Model_Name");
		Category category = new Category(1L, "Category_Name");
		Year year = Year.of(2025);
		String objectId = "ObjectId";
		Car car = new Car(id, make, model, category, year, objectId);
		CarDTO expected = new CarDTO(1L,"Make_Name", "Model_Name", "Category_Name", 2025, "ObjectId");
		
		when(carRepository.findById(anyLong())).thenReturn(Optional.of(car));
		when(mapper.carToCarDto(any(Car.class))).thenReturn(expected);
		
		CarDTO actual = carService.findCarById(id);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(carRepository, times(1)).findById(anyLong());
		verify(mapper, times(1)).carToCarDto(any(Car.class));
	}
	
	@Test
	void createCar_ValidValue_CalledMethodAndReturnsExpected() {

		MakeDTO makeDto = new MakeDTO(1L, "Name");
		ModelDTO modelDto = new ModelDTO(1L, "Name");
		CategoryDTO categoryDto = new CategoryDTO(1L, "Name");
		Make make = new Make(1L, "Name");
		Model model = new Model(1L, "Name");
		Category category = new Category(1L, "Name");
		Year year = Year.of(2025);
		String objectId = "ObjectId";
		Car car = new Car(make, model, category, year, objectId);
		CreateCarDTO createCarDto = new CreateCarDTO("Name", "Name", "Name", 2025, "ObjectId");
		CarDTO expected = new CarDTO(1L, "Name", "Name", "Name", 2025, "ObjectId");
		
		when(makeService.findByNameOrSaveNew(anyString())).thenReturn(makeDto);
		when(makeMapper.dtoToMake(any(MakeDTO.class))).thenReturn(make);
		when(modelService.findByNameOrSaveNew(anyString())).thenReturn(modelDto);
		when(modelMapper.dtoToModel(any(ModelDTO.class))).thenReturn(model);
		when(categoryService.findByNameOrSaveNew(anyString())).thenReturn(categoryDto);
		when(categoryMapper.dtoToCategory(any(CategoryDTO.class))).thenReturn(category);
		when(carRepository.save(any(Car.class))).thenReturn(car);
		when(mapper.carToCarDto(any(Car.class))).thenReturn(expected);

		CarDTO actual = carService.createCar(createCarDto);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(makeService, times(1)).findByNameOrSaveNew(anyString());
		verify(makeMapper, times(1)).dtoToMake(any(MakeDTO.class));
		verify(modelService, times(1)).findByNameOrSaveNew(anyString());
		verify(modelMapper, times(1)).dtoToModel(any(ModelDTO.class));
		verify(categoryService, times(1)).findByNameOrSaveNew(anyString());
		verify(categoryMapper, times(1)).dtoToCategory(any(CategoryDTO.class));
		verify(carRepository, times(1)).save(any(Car.class));
		verify(mapper, times(1)).carToCarDto(any(Car.class));
	}
	
	@Test
	void updateCar_ValidCar_CalledMethodsAndReturnsExpected() {
		
		Long id = 1L;
		Make make = new Make(1L, "Name");
		Model model = new Model(1L, "Name");
		Category category = new Category(1L, "Name");
		Year year = Year.of(2025);
		String objectId = "ObjectId";
		Car car = new Car(make, model, category, year, objectId);
		Make updatedMake = new Make(1L, "Make_Name");
		Model updatedModel = new Model(1L, "Model_Name");
		Category updatedCategory = new Category(1L, "Category_Name");
		Year updatedYear = Year.of(2026);
		String updatedObjectId = "ObjectId";
		Car updatedCar = new Car(1L, updatedMake, updatedModel, updatedCategory, updatedYear, updatedObjectId);
		UpdateCarDTO updateCarDto = new UpdateCarDTO("Make_Name", "Model_Name", "Category_Name", 2026);
		CarDTO expected = new CarDTO(1L, "Make_Name", "Model_Name", "Category_Name", 2026, "ObjectId");
		
		when(carRepository.findById(anyLong())).thenReturn(Optional.of(car));
		when(carRepository.save(any(Car.class))).thenReturn(updatedCar);
		when(mapper.carToCarDto(any(Car.class))).thenReturn(expected);
		
		CarDTO actual = carService.updateCar(id, updateCarDto);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(carRepository, times(1)).findById(anyLong());
		verify(carRepository, times(1)).save(any(Car.class));
		verify(mapper, times(1)).carToCarDto(any(Car.class));
	}
	
	@Test
	void delete_ValidId_CalledMethods() {
		
		Long id = 1L;
		Make make = new Make(1L, "Name");
		Model model = new Model(1L, "Name");
		Category category = new Category(1L, "Name");
		Year year = Year.of(2025);
		String objectId = "ObjectId";
		Car car = new Car(make, model, category, year, objectId);
		
		when(carRepository.findById(anyLong())).thenReturn(Optional.of(car));
		doNothing().when(carRepository).delete(any(Car.class));
		
		carService.delete(id);
		
		verify(carRepository, times(1)).findById(anyLong());
		verify(carRepository, times(1)).delete(any(Car.class));
	}


	@Test
	void filterCars_ValidValue_CalledMethodAndReturnsExpected() {

		Long id = 1L;
		Make make = new Make(1L, "Make_Name");
		Model model = new Model(1L, "Model_Name");
		Category category = new Category(1L, "Category_Name");
		Year year = Year.of(2025);
		String objectId = "ObjectId";
		Car car = new Car(id, make, model, category, year, objectId);
		CarListItemDTO carDto = new CarListItemDTO("Make_Name", "Model_Name", "Category_Name", 2025);
		String makeName = "Make_Name";
		String modelName = "Model_Name";
		String categoryName = "Category_Name";
		Integer yearValue = 2025;
		int page = 0;
		int size = 10;
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));

		Page<Car> carPage = new PageImpl<>(List.of(car));
		Page<CarListItemDTO> expected = new PageImpl<>(List.of(carDto));

		when(carRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(carPage);
		when(mapper.carToCarListItemDto(any(Car.class))).thenReturn(carDto);

		Page<CarListItemDTO> actual = carService.filterCars(makeName, modelName, categoryName, yearValue, pageable);

		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(carRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
		verify(mapper, times(1)).carToCarListItemDto(any(Car.class));

	}
	
	@Test
	void deleteMakeAndAssociations_ValidValue_CalledMethod() {
		
		Long id = 1L;
		Make make = new Make(1L, "Make_Name");
		Model model = new Model(1L, "Model_Name");
		Category category = new Category(1L, "Category_Name");
		Year year = Year.of(2025);
		String objectId = "ObjectId";
		Car car = new Car(id, make, model, category, year, objectId);
		List<Car> cars = List.of(car);
		
		when(carRepository.findByMake_Id(anyLong())).thenReturn(cars);
		doNothing().when(carRepository).delete(any(Car.class));
		doNothing().when(makeService).delete(anyLong());
		
		carService.deleteMakeAndAssociations(id);
		
		verify(carRepository, times(1)).findByMake_Id(anyLong());
		verify(carRepository, times(1)).delete(any(Car.class));
		verify(makeService, times(1)).delete(anyLong());
	}

	@Test
	void deleteModelAndAssociations_ValidValue_CalledMethod() {
		
		Long id = 1L;
		Make make = new Make(1L, "Make_Name");
		Model model = new Model(1L, "Model_Name");
		Category category = new Category(1L, "Category_Name");
		Year year = Year.of(2025);
		String objectId = "ObjectId";
		Car car = new Car(id, make, model, category, year, objectId);
		List<Car> cars = List.of(car);
		
		when(carRepository.findByModel_Id(anyLong())).thenReturn(cars);
		doNothing().when(carRepository).delete(any(Car.class));
		doNothing().when(modelService).delete(anyLong());
		
		carService.deleteModelAndAssociations(id);
		
		verify(carRepository, times(1)).findByModel_Id(anyLong());
		verify(carRepository, times(1)).delete(any(Car.class));
		verify(modelService, times(1)).delete(anyLong());
	}

	@Test
	void deleteCategoryAndAssociations_ValidValue_CalledMethod() {
		
		Long id = 1L;
		Make make = new Make(1L, "Make_Name");
		Model model = new Model(1L, "Model_Name");
		Category category = new Category(1L, "Category_Name");
		Year year = Year.of(2025);
		String objectId = "ObjectId";
		Car car = new Car(id, make, model, category, year, objectId);
		List<Car> cars = List.of(car);
		
		when(carRepository.findByCategory_Id(anyLong())).thenReturn(cars);
		doNothing().when(carRepository).delete(any(Car.class));
		doNothing().when(categoryService).delete(anyLong());
		
		carService.deleteCategoryAndAssociations(id);

		verify(carRepository, times(1)).findByCategory_Id(anyLong());
		verify(carRepository, times(1)).delete(any(Car.class));
		verify(categoryService, times(1)).delete(anyLong());
	}


	@Test
	void generateObjectId_ValidValue_CalledMethodAndReturnsExpected() {

		String expected = "ABCDF";

		try (MockedStatic<NanoIdUtils> mockedStatic = mockStatic(NanoIdUtils.class)) {
			mockedStatic.when(() -> NanoIdUtils.randomNanoId(any(Random.class), any(char[].class), anyInt()))
					.thenReturn("ABCDE")
					.thenReturn("ABCDF");
			when(carRepository.existsByObjectId(anyString())).thenReturn(true)
															 .thenReturn(false);

			String actual = carService.generateObjectId();

			assertThat(actual).isEqualTo(expected);
			verify(carRepository, times(2)).existsByObjectId(anyString());
		}
	}
}
