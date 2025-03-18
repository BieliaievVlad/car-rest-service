package com.foxminded.tasks.car_rest_service.mapper;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import com.foxminded.tasks.car_rest_service.dto.CategoryDTO;
import com.foxminded.tasks.car_rest_service.entity.Category;

@ExtendWith(MockitoExtension.class)
class CategoryMapperTest {

	@InjectMocks
	CategoryMapper mapper;
	
	@Test
	void categoryToDto_ValidCategory_ReturnsExpected() {
		
		Category category = new Category(1L, "Name");
		CategoryDTO expected = new CategoryDTO(1L, "Name");
		
		CategoryDTO actual = mapper.categoryToDto(category);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
	}

	@Test
	void dtoToCategory_ValidDto_ReturnsExpected() {
		
		Category expected = new Category(1L, "Name");
		CategoryDTO categoryDto = new CategoryDTO(1L, "Name");
		
		Category actual = mapper.dtoToCategory(categoryDto);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
	}

}
