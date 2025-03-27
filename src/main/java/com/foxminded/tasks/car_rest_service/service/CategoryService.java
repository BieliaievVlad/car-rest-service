package com.foxminded.tasks.car_rest_service.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.foxminded.tasks.car_rest_service.dto.category.CategoryDTO;
import com.foxminded.tasks.car_rest_service.dto.category.UpsertCategoryDTO;
import com.foxminded.tasks.car_rest_service.entity.Category;
import com.foxminded.tasks.car_rest_service.mapper.CategoryMapper;
import com.foxminded.tasks.car_rest_service.repository.CategoryRepository;
import com.foxminded.tasks.car_rest_service.specification.CategorySpecification;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoryService {
	
	private final CategoryRepository categoryRepository;
	private final CategoryMapper mapper;
	Logger logger = LoggerFactory.getLogger(CategoryService.class);
	
	@Autowired
	public CategoryService(CategoryRepository categoryRepository, CategoryMapper mapper) {
		this.categoryRepository = categoryRepository;
		this.mapper = mapper;
	}
	
	public boolean existsByName(String name) {
		
		return categoryRepository.existsByName(name);
	}
	
	public CategoryDTO findByNameOrSaveNew(String name) {
		
		Category category = categoryRepository.findByName(name)
				.orElseGet(() -> categoryRepository.save(new Category(name)));
		
		return mapper.categoryToDto(category);
	}
	
	public CategoryDTO findById(Long id) {
		
		Optional<Category> optCategory = categoryRepository.findById(id);
		
		if(!optCategory.isEmpty()) {
			
			return mapper.categoryToDto(optCategory.get());
			
		} else {
			logger.error("Category with id {} is not found.", id);
			throw new EntityNotFoundException();
		}	
	}
	
	public CategoryDTO createCategory(UpsertCategoryDTO createCategoryDto) {
		
		if(!existsByName(createCategoryDto.getName())) {
			
			Category newCategory = categoryRepository.save(new Category(createCategoryDto.getName()));
			
			return mapper.categoryToDto(newCategory);
			
		} else {
			logger.error("Category with name {} is already exists.", createCategoryDto.getName());
			throw new IllegalArgumentException();
		}	
	}
	
	public CategoryDTO updateCategory(Long id, UpsertCategoryDTO updateCategoryDto) {
		
		Optional<Category> optCategoryToUpdate = categoryRepository.findById(id);
		
		if(!optCategoryToUpdate.isEmpty()) {
			
			Category categoryToUpdate = optCategoryToUpdate.get();
			categoryToUpdate.setName(updateCategoryDto.getName());
			Category updatedCategory = categoryRepository.save(categoryToUpdate);
			
			return mapper.categoryToDto(updatedCategory);
			
		} else {
			logger.error("Category with id {} is not found.", id);
			throw new EntityNotFoundException();
		}
	}
	
	public void delete(Long id) {
		
		CategoryDTO categoryDto = findById(id);
		Category category = mapper.dtoToCategory(categoryDto);
		categoryRepository.delete(category);
	}
		
	public Page<CategoryDTO> filterCategories(String name, Pageable pageable) {
		
		if(name == null || name.isEmpty()) {
			name = null;
		}
		
		Specification<Category> specification = Specification.where(CategorySpecification.filterByName(name));
		
		Page<Category> categoriesPage = categoryRepository.findAll(specification, pageable);
		
		List<CategoryDTO> categoriesDto = categoriesPage.getContent().stream()
				.map(mapper :: categoryToDto)
				.collect(Collectors.toList());
		
		return new PageImpl<>(categoriesDto, pageable, categoriesPage.getTotalElements());
	}
}
