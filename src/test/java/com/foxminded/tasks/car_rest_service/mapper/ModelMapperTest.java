package com.foxminded.tasks.car_rest_service.mapper;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import com.foxminded.tasks.car_rest_service.dto.ModelDTO;
import com.foxminded.tasks.car_rest_service.entity.Model;

@SpringBootTest
class ModelMapperTest {
	
	@InjectMocks
	ModelMapper mapper;

	@Test
	void modelToDto_ValidModel_ReturnsExpected() {
		
		Model model = new Model(1L, "Name");
		ModelDTO expected = new ModelDTO(1L, "Name");
		
		ModelDTO actual = mapper.modelToDto(model);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
	}

	@Test
	void dtoToModel_ValidDto_ReturnsExpected() {
		
		Model expected = new Model(1L, "Name");
		ModelDTO modelDto = new ModelDTO(1L, "Name");
		
		Model actual = mapper.dtoToModel(modelDto);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
	}

}
