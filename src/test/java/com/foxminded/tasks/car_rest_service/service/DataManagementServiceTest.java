package com.foxminded.tasks.car_rest_service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Year;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import com.foxminded.tasks.car_rest_service.entity.Car;
import com.foxminded.tasks.car_rest_service.entity.Category;
import com.foxminded.tasks.car_rest_service.entity.Make;
import com.foxminded.tasks.car_rest_service.entity.Model;

@SpringBootTest
class DataManagementServiceTest {

	@Mock
	CarService carService;
	
	@Mock
	MakeService makeService;
	
	@Mock
	ModelService modelService;
	
	@Mock
	CategoryService categoryService;
	
	@InjectMocks
	DataManagementService service;
	
	@Test
	void deleteCarById_ValidValue_CalledMethod() {
		
		Long id = 1L;
		Make make = new Make(1L, "Name");
		Model model = new Model(1L, "Name");
		Category category = new Category(1L, "Name");
		Year year = Year.of(2025);
		String objectId = "ObjectId";
		Car car = new Car(make, model, category, year, objectId);
		
		when(carService.findById(anyLong())).thenReturn(car);
		doNothing().when(carService).delete(any(Car.class));
		
		service.deleteCarById(id);
		
		verify(carService, times(1)).findById(anyLong());
		verify(carService).delete(any(Car.class));
	}

	@Test
	void createCar_ValidValue_CalledMethodAndReturnsExpected() {

		Make make = new Make(1L, "Name");
		Model model = new Model(1L, "Name");
		Category category = new Category(1L, "Name");
		Year year = Year.of(2025);
		String objectId = "";
		String generatedObjectId = "ObjectId";
		Car expected = new Car(make, model, category, year, objectId);
		
		when(makeService.findByNameOrSaveNew(anyString())).thenReturn(make);
		when(modelService.findByNameOrSaveNew(anyString())).thenReturn(model);
		when(categoryService.findByNameOrSaveNew(anyString())).thenReturn(category);
		when(carService.generateObjectId()).thenReturn(generatedObjectId);
		when(carService.save(any(Car.class))).thenReturn(expected);
		
		Car actual = service.createCar(expected);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(makeService, times(1)).findByNameOrSaveNew(anyString());
		verify(modelService, times(1)).findByNameOrSaveNew(anyString());
		verify(categoryService, times(1)).findByNameOrSaveNew(anyString());
		verify(carService, times(1)).generateObjectId();
		verify(carService, times(1)).save(any(Car.class));
	}

	@Test
	void createMake_ValidValue_CalledMethodAndReturnsExpected() {
		
		Make expected = new Make(1L, "Name");
		
		when(makeService.existsByName(anyString())).thenReturn(false);
		when(makeService.save(any(Make.class))).thenReturn(expected);
		
		Make actual = service.createMake(expected);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(makeService, times(1)).existsByName(anyString());
		verify(makeService, times(1)).save(any(Make.class));
	}
	
	@Test
	void createMake_InvalidValue_CalledMethodAndReturnsExpected() {
		
		Make expected = new Make(1L, "Name");
		
		when(makeService.existsByName(anyString())).thenReturn(true);
		
		assertThrows(IllegalArgumentException.class, () -> {
			service.createMake(expected);
		});
		verify(makeService, times(1)).existsByName(anyString());
	}

	@Test
	void createModel_ValidValue_CalledMethodAndReturnsExpected() {
		
		Model expected = new Model(1L, "Name");
		
		when(modelService.existsByName(anyString())).thenReturn(false);
		when(modelService.save(any(Model.class))).thenReturn(expected);
		
		Model actual = service.createModel(expected);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(modelService, times(1)).existsByName(anyString());
		verify(modelService, times(1)).save(any(Model.class));
	}
	
	@Test
	void createModel_InvalidValue_CalledMethodAndReturnsExpected() {
		
		Model expected = new Model(1L, "Name");
		
		when(modelService.existsByName(anyString())).thenReturn(true);
		
		assertThrows(IllegalArgumentException.class, () -> {
			service.createModel(expected);
		});
		verify(modelService, times(1)).existsByName(anyString());
	}

	@Test
	void createCategory_ValidValue_CalledMethodAndReturnsExpected() {
		
		Category expected = new Category(1L, "Name");
		
		when(categoryService.existsByName(anyString())).thenReturn(false);
		when(categoryService.save(any(Category.class))).thenReturn(expected);
		
		Category actual = service.createCategory(expected);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(categoryService, times(1)).existsByName(anyString());
		verify(categoryService, times(1)).save(any(Category.class));
	}
	
	@Test
	void createCategory_InvalidValue_CalledMethodAndReturnsExpected() {
		
		Category expected = new Category(1L, "Name");
		
		when(categoryService.existsByName(anyString())).thenReturn(true);
		
		assertThrows(IllegalArgumentException.class, () -> {
			service.createCategory(expected);
		});
		verify(categoryService, times(1)).existsByName(anyString());
	}

	@Test
	void deleteMakeAndAssociations_ValidValue_CalledMethod() {
		
		Long id = 1L;
		Make make = new Make(1L, "Name");
		List<Car> cars = List.of(new Car());
		
		when(makeService.findById(anyLong())).thenReturn(make);
		when(carService.findByMake(any(Make.class))).thenReturn(cars);
		doNothing().when(carService).delete(any(Car.class));
		doNothing().when(makeService).delete(any(Make.class));
		
		service.deleteMakeAndAssociations(id);
		
		verify(makeService, times(1)).findById(anyLong());
		verify(carService, times(1)).findByMake(any(Make.class));
		verify(carService, times(1)).delete(any(Car.class));
		verify(makeService, times(1)).delete(any(Make.class));
	}

	@Test
	void deleteModelAndAssociations_ValidValue_CalledMethod() {
		
		Long id = 1L;
		Model model = new Model(1L, "Name");
		List<Car> cars = List.of(new Car());
		
		when(modelService.findById(anyLong())).thenReturn(model);
		when(carService.findByModel(any(Model.class))).thenReturn(cars);
		doNothing().when(carService).delete(any(Car.class));
		doNothing().when(modelService).delete(any(Model.class));
		
		service.deleteModelAndAssociations(id);
		
		verify(modelService, times(1)).findById(anyLong());
		verify(carService, times(1)).findByModel(any(Model.class));
		verify(carService, times(1)).delete(any(Car.class));
		verify(modelService, times(1)).delete(any(Model.class));
	}

	@Test
	void deleteCategoryAndAssociations_ValidValue_CalledMethod() {
		
		Long id = 1L;
		Category category = new Category(1L, "Name");
		List<Car> cars = List.of(new Car());
		
		when(categoryService.findById(anyLong())).thenReturn(category);
		when(carService.findByCategory(any(Category.class))).thenReturn(cars);
		doNothing().when(carService).delete(any(Car.class));
		doNothing().when(categoryService).delete(any(Category.class));
		
		service.deleteCategoryAndAssociations(id);
		
		verify(categoryService, times(1)).findById(anyLong());
		verify(carService, times(1)).findByCategory(any(Category.class));
		verify(carService, times(1)).delete(any(Car.class));
		verify(categoryService, times(1)).delete(any(Category.class));
	}

}
