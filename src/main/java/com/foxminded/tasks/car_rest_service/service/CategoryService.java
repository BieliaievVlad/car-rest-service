package com.foxminded.tasks.car_rest_service.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.foxminded.tasks.car_rest_service.entity.Category;
import com.foxminded.tasks.car_rest_service.repository.CategoryRepository;
import com.foxminded.tasks.car_rest_service.specification.CategorySpecification;

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

		Optional<Category> optCategory = categoryRepository.findById(id);

		if (optCategory.isPresent()) {
			return optCategory.get();

		} else {
			logger.error("Category with id {} is not found.", id);
			throw new EntityNotFoundException();
		}

	}
	
	public Category save(Category category) {

		if (!isCategoryValid(category)) {
			logger.error("Save error. Category is not valid.");
			throw new IllegalArgumentException();

		} else {
			return categoryRepository.save(category);
		}

	}
	
	public void delete(Category category) {

		if (!isCategoryValid(category)) {
			logger.error("Delete error. Category is not valid.");
			throw new IllegalArgumentException();

		} else {
			categoryRepository.delete(category);
		}

	}
	
	public boolean existsByName(String name) {
		
		return categoryRepository.existsByName(name);
	}
	
	public Category findByNameOrSaveNew(String name) {
		
		return categoryRepository.findByName(name)
				.orElseGet(() -> categoryRepository.save(new Category(name)));
	}
	
	public Page<Category> filterCategories(String name, Pageable pageable) {
		
		if(name == null || name.isEmpty()) {
			name = null;
		}
		
		Specification<Category> specification = Specification.where(CategorySpecification.filterByName(name));
		
		return categoryRepository.findAll(specification, pageable);
	}
	
	private boolean isCategoryValid(Category category) {
		
		return category != null &&
			   category.getName() != null;
	}
}
