package com.foxminded.tasks.car_rest_service.mapper;

import static org.assertj.core.api.Assertions.*;
import java.time.Year;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import com.foxminded.tasks.car_rest_service.dto.CarDTO;
import com.foxminded.tasks.car_rest_service.entity.Car;
import com.foxminded.tasks.car_rest_service.entity.Category;
import com.foxminded.tasks.car_rest_service.entity.Make;
import com.foxminded.tasks.car_rest_service.entity.Model;

@ExtendWith(MockitoExtension.class)
class CarMapperTest {
	
	@InjectMocks
	CarMapper mapper;

	@Test
	void carToDto_ValidCar_ReturnsExpected() {
		
		Long id = 1L;
		Make make = new Make(1L, "Make_Name");
		Model model = new Model(1L, "Model_Name");
		Category category = new Category(1L, "Category_Name");
		Year year = Year.of(2025);
		String objectId = "ObjectId";
		Car car = new Car(id, make, model, category, year, objectId);
		CarDTO expected = new CarDTO(1L, "Make_Name", "Model_Name", "Category_Name", 2025, "ObjectId");
		
		CarDTO actual = mapper.carToDto(car);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
	}

	@Test
	void dtoToCar_ValidDto_ReturnsExpected() {
		
		CarDTO carDTO = new CarDTO(1L, "Make_Name", "Model_Name", "Category_Name", 2025, "ObjectId");
		Long id = 1L;
		Make make = new Make(1L, "Make_Name");
		Model model = new Model(1L, "Model_Name");
		Category category = new Category(1L, "Category_Name");
		Year year = Year.of(2025);
		String objectId = "ObjectId";
		Car expected = new Car(id, make, model, category, year, objectId);

		Car actual = mapper.dtoToCar(carDTO, make, model, category);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
	}

}
