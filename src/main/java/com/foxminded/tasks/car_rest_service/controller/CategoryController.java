package com.foxminded.tasks.car_rest_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foxminded.tasks.car_rest_service.dto.CategoryDTO;
import com.foxminded.tasks.car_rest_service.service.CategoryService;
import com.foxminded.tasks.car_rest_service.service.DataManagementService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("api/v1")
public class CategoryController {
	
	private final CategoryService categoryService;
	private final DataManagementService service;
	
	@Autowired
	public CategoryController(CategoryService categoryService, DataManagementService service) {
		this.categoryService = categoryService;
		this.service = service;
	}
	
	@GetMapping("/categories")
	public Page<CategoryDTO> getFilteredCategories(@RequestParam(required = false) String name,
								 				@RequestParam(defaultValue = "0") int page,
								 				@RequestParam(defaultValue = "10") int size) {
		
    	Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
        return categoryService.filterCategories(name, pageable);
	}
	
	@GetMapping("/categories/{id}")
	public ResponseEntity<CategoryDTO> getCategory(@PathVariable Long id) {
		
		try {
			CategoryDTO categoryDto = service.findCategoryById(id);
			return new ResponseEntity<>(categoryDto, HttpStatus.OK);
			
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/categories")
	public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDto) {

		try {
			service.createCategory(categoryDto);
			return new ResponseEntity<>(categoryDto, HttpStatus.CREATED);
			
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/categories/{id}")
	public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {

		try {
			service.deleteCategoryAndAssociations(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

}
