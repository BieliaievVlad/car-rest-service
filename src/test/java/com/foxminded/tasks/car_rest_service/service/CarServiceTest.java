package com.foxminded.tasks.car_rest_service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.foxminded.tasks.car_rest_service.entity.Car;
import com.foxminded.tasks.car_rest_service.entity.Category;
import com.foxminded.tasks.car_rest_service.entity.Make;
import com.foxminded.tasks.car_rest_service.entity.Model;
import com.foxminded.tasks.car_rest_service.repository.CarRepository;

@SpringBootTest
class CarServiceTest {

	@InjectMocks
	CarService carService;

	@Mock
	CarRepository carRepository;

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
	void filterCars_ValidValue_CalledMethodAndReturnsExpected() {

		Long id = 1L;
		Make make = new Make(1L, "Make_Name");
		Model model = new Model(1L, "Model_Name");
		Category category = new Category(1L, "Category_Name");
		Year year = Year.of(2025);
		String objectId = "ObjectId";
		Car car = new Car(id, make, model, category, year, objectId);

		String makeName = "Make_Name";
		String modelName = "Model_Name";
		String categoryName = "Category_Name";
		Integer yearValue = 2025;
		int page = 0;
		int size = 10;
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));

		Page<Car> expected = new PageImpl<>(List.of(car));

		when(carRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(expected);

		Page<Car> actual = carService.filterCars(makeName, modelName, categoryName, yearValue, pageable);

		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(carRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));

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
