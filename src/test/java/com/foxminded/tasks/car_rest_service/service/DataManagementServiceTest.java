package com.foxminded.tasks.car_rest_service.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Year;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

@ExtendWith(MockitoExtension.class)
class DataManagementServiceTest {

	@Mock
	CarService carService;
	
	@Mock
	MakeService makeService;
	
	@Mock
	ModelService modelService;
	
	@Mock
	CategoryService categoryService;
	
	@Mock
	CarMapper carMapper;
	
	@Mock
	MakeMapper makeMapper;
	
	@Mock
	ModelMapper modelMapper;
	
	@Mock
	CategoryMapper categoryMapper;
	
	@InjectMocks
	DataManagementService service;
	
	@Test
	void findAllCars_ValidValue_CalledMethodsAndReturnsExpected() {
		
		Long id = 1L;
		Make make = new Make(1L, "Name");
		Model model = new Model(1L, "Name");
		Category category = new Category(1L, "Name");
		Year year = Year.of(2025);
		String objectId = "ObjectId";
		Car car = new Car(id, make, model, category, year, objectId);
		CarDTO carDto = new CarDTO(1L, "Name", "Name", "Name", 2025, "ObjectId");
		List<CarDTO> expected = List.of(carDto);
		
		when(carService.findAll()).thenReturn(List.of(car));
		when(carMapper.carToDto(any(Car.class))).thenReturn(carDto);
		
		List<CarDTO> actual = service.findAllCars();
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(carService, times(1)).findAll();
		verify(carMapper, times(1)).carToDto(any(Car.class));
	}
	
	@Test
	void findAllMakes_ValidValue_CalledMethodsAndReturnsExpected() {
		
		Make make = new Make(1L, "Name");
		MakeDTO makeDto = new MakeDTO(1L, "Name");
		List<MakeDTO> expected = List.of(makeDto);
		
		when(makeService.findAll()).thenReturn(List.of(make));
		when(makeMapper.makeToDto(any(Make.class))).thenReturn(makeDto);
		
		List<MakeDTO> actual = service.findAllMakes();
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(makeService, times(1)).findAll();
		verify(makeMapper, times(1)).makeToDto(any(Make.class));	
	}
	
	@Test
	void findAllModels_ValidValue_CalledMethodsAndReturnsExpected() {
		
		Model model = new Model(1L, "Name");
		ModelDTO modelDto = new ModelDTO(1L, "Name");
		List<ModelDTO> expected = List.of(modelDto);
		
		when(modelService.findAll()).thenReturn(List.of(model));
		when(modelMapper.modelToDto(any(Model.class))).thenReturn(modelDto);
		
		List<ModelDTO> actual = service.findAllModels();
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(modelService, times(1)).findAll();
		verify(modelMapper, times(1)).modelToDto(any(Model.class));
	}
	@Test
	void findAllCategories_ValidValue_CalledMethodsAndReturnsExpected() {
		
		Category category = new Category(1L, "Name");
		CategoryDTO categoryDto = new CategoryDTO(1L, "Name");
		List<CategoryDTO> expected = List.of(categoryDto);
		
		when(categoryService.findAll()).thenReturn(List.of(category));
		when(categoryMapper.categoryToDto(any(Category.class))).thenReturn(categoryDto);
		
		List<CategoryDTO> actual = service.findAllCategories();
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(categoryService, times(1)).findAll();
		verify(categoryMapper, times(1)).categoryToDto(any(Category.class));
	}
	
	@Test
	void findCarById_ValidId_CalledMethodsAndReturnsExpected() {
		
		Long id = 1L;
		Make make = new Make(1L, "Name");
		Model model = new Model(1L, "Name");
		Category category = new Category(1L, "Name");
		Year year = Year.of(2025);
		String objectId = "ObjectId";
		Car car = new Car(id, make, model, category, year, objectId);
		CarDTO expected = new CarDTO(1L, "Name", "Name", "Name", 2025, "ObjectId");
		
		when(carService.findById(anyLong())).thenReturn(car);
		when(carMapper.carToDto(any(Car.class))).thenReturn(expected);
		
		CarDTO actual = service.findCarById(id);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(carService, times(1)).findById(anyLong());
		verify(carMapper, times(1)).carToDto(any(Car.class));
	}
	@Test
	void findMakeById_ValidId_CalledMethodsAndReturnsExpected() {
		
		Long id = 1L;
		Make make = new Make(1L, "Name");
		MakeDTO expected = new MakeDTO(1L, "Name");
		
		when(makeService.findById(anyLong())).thenReturn(make);
		when(makeMapper.makeToDto(any(Make.class))).thenReturn(expected);
		
		MakeDTO actual = service.findMakeById(id);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(makeService, times(1)).findById(anyLong());
		verify(makeMapper, times(1)).makeToDto(any(Make.class));
	}
	@Test
	void findModelById_ValidId_CalledMethodsAndReturnsExpected() {
		
		Long id = 1L;
		Model model = new Model(1L, "Name");
		ModelDTO expected = new ModelDTO(1L, "Name");
		
		when(modelService.findById(anyLong())).thenReturn(model);
		when(modelMapper.modelToDto(any(Model.class))).thenReturn(expected);
		
		ModelDTO actual = service.findModelById(id);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(modelService, times(1)).findById(anyLong());
		verify(modelMapper, times(1)).modelToDto(any(Model.class));
	}
	@Test
	void findCategoryById_ValidId_CalledMethodsAndReturnsExpected() {
		
		long id = 1L;
		Category category = new Category(1L, "Name");
		CategoryDTO expected = new CategoryDTO(1L, "Name");
		
		when(categoryService.findById(anyLong())).thenReturn(category);
		when(categoryMapper.categoryToDto(any(Category.class))).thenReturn(expected);
		
		CategoryDTO actual = service.findCategoryById(id);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(categoryService, times(1)).findById(anyLong());
		verify(categoryMapper, times(1)).categoryToDto(any(Category.class));
	}
	
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
		CarDTO carDto = new CarDTO(1L, "Name", "Name", "Name", 2025, "");
		
		when(makeService.findByNameOrSaveNew(anyString())).thenReturn(make);
		when(modelService.findByNameOrSaveNew(anyString())).thenReturn(model);
		when(categoryService.findByNameOrSaveNew(anyString())).thenReturn(category);
		when(carService.generateObjectId()).thenReturn(generatedObjectId);
		when(carService.save(any(Car.class))).thenReturn(expected);
		
		Car actual = service.createCar(carDto);
		
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
		MakeDTO makeDto = new MakeDTO(1L, "Name");
		
		when(makeService.existsByName(anyString())).thenReturn(false);
		when(makeService.save(any(Make.class))).thenReturn(expected);
		
		Make actual = service.createMake(makeDto);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(makeService, times(1)).existsByName(anyString());
		verify(makeService, times(1)).save(any(Make.class));
	}
	
	@Test
	void createMake_InvalidValue_CalledMethodAndReturnsExpected() {

		MakeDTO makeDto = new MakeDTO(1L, "Name");
		
		when(makeService.existsByName(anyString())).thenReturn(true);
		
		assertThrows(IllegalArgumentException.class, () -> {
			service.createMake(makeDto);
		});
		verify(makeService, times(1)).existsByName(anyString());
	}

	@Test
	void createModel_ValidValue_CalledMethodAndReturnsExpected() {
		
		Model expected = new Model(1L, "Name");
		ModelDTO modelDto = new ModelDTO(1L, "Name");
		
		when(modelService.existsByName(anyString())).thenReturn(false);
		when(modelService.save(any(Model.class))).thenReturn(expected);
		
		Model actual = service.createModel(modelDto);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(modelService, times(1)).existsByName(anyString());
		verify(modelService, times(1)).save(any(Model.class));
	}
	
	@Test
	void createModel_InvalidValue_CalledMethodAndReturnsExpected() {

		ModelDTO modelDto = new ModelDTO(1L, "Name");
		
		when(modelService.existsByName(anyString())).thenReturn(true);
		
		assertThrows(IllegalArgumentException.class, () -> {
			service.createModel(modelDto);
		});
		verify(modelService, times(1)).existsByName(anyString());
	}

	@Test
	void createCategory_ValidValue_CalledMethodAndReturnsExpected() {
		
		Category expected = new Category(1L, "Name");
		CategoryDTO categoryDto = new CategoryDTO(1L, "Name");
		
		when(categoryService.existsByName(anyString())).thenReturn(false);
		when(categoryService.save(any(Category.class))).thenReturn(expected);
		
		Category actual = service.createCategory(categoryDto);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(categoryService, times(1)).existsByName(anyString());
		verify(categoryService, times(1)).save(any(Category.class));
	}
	
	@Test
	void createCategory_InvalidValue_CalledMethodAndReturnsExpected() {

		CategoryDTO categoryDto = new CategoryDTO(1L, "Name");
		
		when(categoryService.existsByName(anyString())).thenReturn(true);
		
		assertThrows(IllegalArgumentException.class, () -> {
			service.createCategory(categoryDto);
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
	
	@Test
	void dtoToCar_ValidValue_CalledMethodsAndReturnsExpected() {
		
		Make make = new Make(1L, "Name");
		Model model = new Model(1L, "Name");
		Category category = new Category(1L, "Name");
		Year year = Year.of(2025);
		String objectId = "ObjectId";
		Car expected = new Car(make, model, category, year, objectId);
		CarDTO carDto = new CarDTO(1L, "Name", "Name", "Name", 2025, "ObjectId");
		
		when(makeService.findByNameOrSaveNew(anyString())).thenReturn(make);
		when(modelService.findByNameOrSaveNew(anyString())).thenReturn(model);
		when(categoryService.findByNameOrSaveNew(anyString())).thenReturn(category);
		when(carMapper.dtoToCar(any(CarDTO.class), any(Make.class), any(Model.class), any(Category.class))).thenReturn(expected);
		
		Car actual = service.dtoToCar(carDto);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(makeService, times(1)).findByNameOrSaveNew(anyString());
		verify(modelService, times(1)).findByNameOrSaveNew(anyString());
		verify(categoryService, times(1)).findByNameOrSaveNew(anyString());
		verify(carMapper, times(1)).dtoToCar(any(CarDTO.class), any(Make.class), any(Model.class), any(Category.class));
	}

}
