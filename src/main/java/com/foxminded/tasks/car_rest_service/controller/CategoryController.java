package com.foxminded.tasks.car_rest_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foxminded.tasks.car_rest_service.entity.Category;
import com.foxminded.tasks.car_rest_service.service.CategoryService;
import com.foxminded.tasks.car_rest_service.service.PersistenceService;

@RestController
@RequestMapping("api/v1")
public class CategoryController {
	
	private final CategoryService categoryService;
	private final PersistenceService service;
	
	@Autowired
	public CategoryController(CategoryService categoryService, PersistenceService service) {
		this.categoryService = categoryService;
		this.service = service;
	}
	
	@GetMapping("/categories")
	public Page<Category> getFilteredCategories(@RequestParam(required = false) String name,
								 				@RequestParam(defaultValue = "0") int page,
								 				@RequestParam(defaultValue = "10") int size) {
		
    	Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
        return categoryService.filterCategories(name, pageable);
	}

}
