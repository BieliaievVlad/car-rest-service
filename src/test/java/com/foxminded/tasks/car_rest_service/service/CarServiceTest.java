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
import com.foxminded.tasks.car_rest_service.entity.Car;
import com.foxminded.tasks.car_rest_service.entity.Category;
import com.foxminded.tasks.car_rest_service.entity.Make;
import com.foxminded.tasks.car_rest_service.entity.Model;
import com.foxminded.tasks.car_rest_service.mapper.CarMapper;
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

	@Test
	void findAll_ValidValue_CalledMethodAndReturnsExpected() {

		Long id = 1L;
		Make make = new Make(1L, "Make_Name");
		Model model = new Model(1L, "Model_Name");
		Category category = new Category(1L, "Category_Name");
		Year year = Year.of(2025);
		String objectId = "ObjectId";
		Car car = new Car(id, make, model, category, year, objectId);

		List<Car> expected = List.of(car);

		when(carRepository.findAll()).thenReturn(expected);

		List<Car> actual = carService.findAll();

		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(carRepository, times(1)).findAll();
	}

	@Test
	void findById_ValidValue_CalledMethodAndReturnsExpected() {

		Long id = 1L;
		Make make = new Make(1L, "Make_Name");
		Model model = new Model(1L, "Model_Name");
		Category category = new Category(1L, "Category_Name");
		Year year = Year.of(2025);
		String objectId = "ObjectId";
		Car expected = new Car(id, make, model, category, year, objectId);

		when(carRepository.findById(anyLong())).thenReturn(Optional.of(expected));

		Car actual = carService.findById(id);

		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(carRepository, times(1)).findById(anyLong());
	}

	@Test
	void save_ValidValue_CalledMethodAndReturnsExpected() {

		Long id = 1L;
		Make make = new Make(1L, "Make_Name");
		Model model = new Model(1L, "Model_Name");
		Category category = new Category(1L, "Category_Name");
		Year year = Year.of(2025);
		String objectId = "ObjectId";
		Car expected = new Car(id, make, model, category, year, objectId);

		when(carRepository.save(any(Car.class))).thenReturn(expected);

		Car actual = carService.save(expected);

		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(carRepository, times(1)).save(any(Car.class));
	}

	@Test
	void delete_ValidValue_CalledMethod() {

		Long id = 1L;
		Make make = new Make(1L, "Make_Name");
		Model model = new Model(1L, "Model_Name");
		Category category = new Category(1L, "Category_Name");
		Year year = Year.of(2025);
		String objectId = "ObjectId";
		Car car = new Car(id, make, model, category, year, objectId);

		doNothing().when(carRepository).delete(any(Car.class));

		carService.delete(car);

		verify(carRepository, times(1)).delete(any(Car.class));
	}
	
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

		Make make = new Make(1L, "Name");
		Model model = new Model(1L, "Name");
		Category category = new Category(1L, "Name");
		Year year = Year.of(2025);
		String objectId = "ObjectId";
		Car car = new Car(make, model, category, year, objectId);
		CreateCarDTO createCarDto = new CreateCarDTO("Name", "Name", "Name", 2025, "ObjectId");
		CarDTO expected = new CarDTO(1L, "Name", "Name", "Name", 2025, "ObjectId");
		
		when(makeService.findByNameOrSaveNew(anyString())).thenReturn(make);
		when(modelService.findByNameOrSaveNew(anyString())).thenReturn(model);
		when(categoryService.findByNameOrSaveNew(anyString())).thenReturn(category);
		when(carRepository.save(any(Car.class))).thenReturn(car);
		when(mapper.carToCarDto(any(Car.class))).thenReturn(expected);

		CarDTO actual = carService.createCar(createCarDto);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(makeService, times(1)).findByNameOrSaveNew(anyString());
		verify(modelService, times(1)).findByNameOrSaveNew(anyString());
		verify(categoryService, times(1)).findByNameOrSaveNew(anyString());
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
	void deleteCarById_ValidId_CalledMethods() {
		
		Long id = 1L;
		Make make = new Make(1L, "Name");
		Model model = new Model(1L, "Name");
		Category category = new Category(1L, "Name");
		Year year = Year.of(2025);
		String objectId = "ObjectId";
		Car car = new Car(make, model, category, year, objectId);
		
		when(carRepository.findById(anyLong())).thenReturn(Optional.of(car));
		doNothing().when(carRepository).delete(any(Car.class));
		
		carService.deleteCarById(id);
		
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
	void findByMake_ValidValue_CalledMethodAndReturnsExpected() {

		Long id = 1L;
		Make make = new Make(1L, "Make_Name");
		Model model = new Model(1L, "Model_Name");
		Category category = new Category(1L, "Category_Name");
		Year year = Year.of(2025);
		String objectId = "ObjectId";
		Car car = new Car(id, make, model, category, year, objectId);
		List<Car> expected = List.of(car);

		when(carRepository.findByMake(any(Make.class))).thenReturn(List.of(car));

		List<Car> actual = carService.findByMake(make);

		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(carRepository, times(1)).findByMake(any(Make.class));
	}

	@Test
	void findByModel_ValidValue_CalledMethodAndReturnsExpected() {

		Long id = 1L;
		Make make = new Make(1L, "Make_Name");
		Model model = new Model(1L, "Model_Name");
		Category category = new Category(1L, "Category_Name");
		Year year = Year.of(2025);
		String objectId = "ObjectId";
		Car car = new Car(id, make, model, category, year, objectId);
		List<Car> expected = List.of(car);

		when(carRepository.findByModel(any(Model.class))).thenReturn(List.of(car));

		List<Car> actual = carService.findByModel(model);

		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(carRepository, times(1)).findByModel(any(Model.class));
	}

	@Test
	void findByCategory_ValidValue_CalledMethodAndReturnsExpected() {

		Long id = 1L;
		Make make = new Make(1L, "Make_Name");
		Model model = new Model(1L, "Model_Name");
		Category category = new Category(1L, "Category_Name");
		Year year = Year.of(2025);
		String objectId = "ObjectId";
		Car car = new Car(id, make, model, category, year, objectId);
		List<Car> expected = List.of(car);

		when(carRepository.findByCategory(any(Category.class))).thenReturn(List.of(car));

		List<Car> actual = carService.findByCategory(category);

		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(carRepository, times(1)).findByCategory(any(Category.class));
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
		
		when(makeService.findById(anyLong())).thenReturn(make);
		when(carRepository.findByMake(any(Make.class))).thenReturn(cars);
		doNothing().when(carRepository).delete(any(Car.class));
		doNothing().when(makeService).delete(any(Make.class));
		
		carService.deleteMakeAndAssociations(id);
		
		verify(makeService, times(1)).findById(anyLong());
		verify(carRepository, times(1)).findByMake(any(Make.class));
		verify(carRepository, times(1)).delete(any(Car.class));
		verify(makeService, times(1)).delete(any(Make.class));
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
		
		when(modelService.findById(anyLong())).thenReturn(model);
		when(carRepository.findByModel(any(Model.class))).thenReturn(cars);
		doNothing().when(carRepository).delete(any(Car.class));
		doNothing().when(modelService).delete(any(Model.class));
		
		carService.deleteModelAndAssociations(id);
		
		verify(modelService, times(1)).findById(anyLong());
		verify(carRepository, times(1)).findByModel(any(Model.class));
		verify(carRepository, times(1)).delete(any(Car.class));
		verify(modelService, times(1)).delete(any(Model.class));
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
		
		when(categoryService.findById(anyLong())).thenReturn(category);
		when(carRepository.findByCategory(any(Category.class))).thenReturn(cars);
		doNothing().when(carRepository).delete(any(Car.class));
		doNothing().when(categoryService).delete(any(Category.class));
		
		carService.deleteCategoryAndAssociations(id);
		
		verify(categoryService, times(1)).findById(anyLong());
		verify(carRepository, times(1)).findByCategory(any(Category.class));
		verify(carRepository, times(1)).delete(any(Car.class));
		verify(categoryService, times(1)).delete(any(Category.class));
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
