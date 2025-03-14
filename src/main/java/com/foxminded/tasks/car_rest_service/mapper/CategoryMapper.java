package com.foxminded.tasks.car_rest_service.mapper;

import org.springframework.stereotype.Component;

import com.foxminded.tasks.car_rest_service.dto.CategoryDTO;
import com.foxminded.tasks.car_rest_service.entity.Category;

@Component
public class CategoryMapper {

	public CategoryDTO categoryToDto(Category category) {
		
		return new CategoryDTO(category.getId(), category.getName());
	}
	
	public Category dtoToCategory(CategoryDTO categoryDto) {
		
		return new Category(categoryDto.getId(), categoryDto.getName());
	}
}
