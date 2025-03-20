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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foxminded.tasks.car_rest_service.dto.category.CategoryDTO;
import com.foxminded.tasks.car_rest_service.dto.category.CreateUpdateCategoryDTO;
import com.foxminded.tasks.car_rest_service.service.CarService;
import com.foxminded.tasks.car_rest_service.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("api/v1")
public class CategoryController {
	
	private final CategoryService service;
	private CarService carService;
	
	@Autowired
	public CategoryController(CategoryService service,
							  CarService carService) {
		this.service = service;
		this.carService = carService;
	}
	
	@GetMapping("/categories")
	public Page<CategoryDTO> getFilteredCategories(@RequestParam(required = false) String name,
								 				@RequestParam(defaultValue = "0") int page,
								 				@RequestParam(defaultValue = "10") int size) {
		
    	Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
        return service.filterCategories(name, pageable);
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
	public ResponseEntity<CategoryDTO> createCategory(@RequestBody CreateUpdateCategoryDTO createCategoryDto) {

		try {
			CategoryDTO categoryDto = service.createCategory(createCategoryDto);
			return new ResponseEntity<>(categoryDto, HttpStatus.CREATED);
			
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/categories/{id}")
	public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CreateUpdateCategoryDTO updateCategoryDto) {
		
		try {
			CategoryDTO categoryDto = service.updateCategory(id, updateCategoryDto);
			return new ResponseEntity<>(categoryDto, HttpStatus.OK);
			
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/categories/{id}")
	public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {

		try {
			carService.deleteCategoryAndAssociations(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

}
