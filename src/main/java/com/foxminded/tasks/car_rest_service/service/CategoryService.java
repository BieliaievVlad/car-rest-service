package com.foxminded.tasks.car_rest_service.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxminded.tasks.car_rest_service.entity.Category;
import com.foxminded.tasks.car_rest_service.repository.CategoryRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoryService {
	
	private final CategoryRepository categoryRepository;
	Logger logger = LoggerFactory.getLogger(CategoryService.class);
	
	@Autowired
	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}
	
	public List<Category> findAll() {
		return categoryRepository.findAll();
	}
	
	public Category findById(Long id) {
		
		try {
			
			Optional<Category> optCategory = categoryRepository.findById(id);
			
			if(optCategory.isPresent()) {
				return optCategory.get();
				
			} else {
				throw new EntityNotFoundException();
			}
		} catch (Exception e) {
			logger.error("Category with id {} is not found.", id);
			return new Category();
		}
	}
	
	public void save(Category category) {
		
		try {
			
			if(!isCategoryValid(category)) {
				throw new IllegalArgumentException();
				
			} else {
				categoryRepository.save(category);
			}
			
		} catch (Exception e) {
			logger.error("Save error. Category is not valid.");
			logger.error("Name: {}", category.getName());
		}
	}
	
	public void delete(Category category) {
		
		try {
			
			if(!isCategoryValid(category)) {
				throw new IllegalArgumentException();
				
			} else {
				categoryRepository.delete(category);
			}
			
		} catch (Exception e) {
			logger.error("Delete error. Category is not valid.");
			logger.error("ID: {}", category.getId());
			logger.error("Name: {}", category.getName());
		}
	}
	
	private boolean isCategoryValid(Category category) {
		
		return category != null &&
			   category.getName() != null;
	}
}
