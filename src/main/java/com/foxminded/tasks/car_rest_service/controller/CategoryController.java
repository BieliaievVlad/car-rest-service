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
import com.foxminded.tasks.car_rest_service.dto.category.UpsertCategoryDTO;
import com.foxminded.tasks.car_rest_service.service.CarService;
import com.foxminded.tasks.car_rest_service.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

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
	
    @Operation(summary = "List Categories with filtering options for name")
    @ApiResponses(value = { 
    		  @ApiResponse(responseCode = "200", description = "List of Categories successfully fetched", 
    		    content = { @Content(mediaType = "application/json", 
    		      schema = @Schema(implementation = CategoryDTO.class)) })
    		  })
	@GetMapping("/categories")
	public Page<CategoryDTO> getFilteredCategories(@RequestParam(required = false) String name,
								 				@RequestParam(defaultValue = "0") int page,
								 				@RequestParam(defaultValue = "10") int size) {
		
    	Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
        return service.filterCategories(name, pageable);
	}
	
    @Operation(summary = "Find a Category with given id")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", description = "Category found", 
    					 content = { @Content(mediaType = "application/json",
    					 schema = @Schema(implementation = CategoryDTO.class))
    		}),
    		@ApiResponse(responseCode = "404", description = "Category not found", content = @Content)
    })
	@GetMapping("/categories/{id}")
	public ResponseEntity<CategoryDTO> getCategory(@Parameter(description = "ID of Category to be searched")
												   @PathVariable Long id) {

		CategoryDTO categoryDto = service.findById(id);
		return new ResponseEntity<>(categoryDto, HttpStatus.OK);
	}
	
    @Operation(summary = "Create a new Category", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "201", description = "Category created and added to DataBase",
    					 content = { @Content(mediaType = "application/json",
    					 schema = @Schema(implementation = CategoryDTO.class))
    					 }),
    		@ApiResponse(responseCode = "400", description = "Category with given name is already exists"),
    		@ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
	@PostMapping("/categories")
	public ResponseEntity<CategoryDTO> createCategory(@io.swagger.v3.oas.annotations.parameters.RequestBody(
															description = "Category to create", required = true, 
															content = @Content(mediaType = "application/json", 
															schema = @Schema(implementation = UpsertCategoryDTO.class),
															examples = @ExampleObject(value = "{\"name\": \"Sedan\"}")))
													  @RequestBody UpsertCategoryDTO createCategoryDto) {

		CategoryDTO categoryDto = service.createCategory(createCategoryDto);
		return new ResponseEntity<>(categoryDto, HttpStatus.CREATED);
	}
	
    @Operation(summary = "Update an existing Category", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", description = "Category updated",
    				content = { @Content(mediaType = "application/json",
    				schema = @Schema(implementation = CategoryDTO.class))
    				}),
    		@ApiResponse(responseCode = "400", description = "Category data is not valid"),
    		@ApiResponse(responseCode = "401", description = "Unauthorized access"),
    		@ApiResponse(responseCode = "404", description = "Unable to update. Category not found", content = @Content)
    })
	@PutMapping("/categories/{id}")
	public ResponseEntity<CategoryDTO> updateCategory(@Parameter(description = "ID of Category to be updated")
													  @PathVariable Long id, 
													  @io.swagger.v3.oas.annotations.parameters.RequestBody(
																description = "Category data to update", required = true, 
																content = @Content(mediaType = "application/json", 
																schema = @Schema(implementation = UpsertCategoryDTO.class),
																examples = @ExampleObject(value = "{\"name\": \"Sedan\"}")))
													  @RequestBody UpsertCategoryDTO updateCategoryDto) {

		CategoryDTO categoryDto = service.updateCategory(id, updateCategoryDto);
		return new ResponseEntity<>(categoryDto, HttpStatus.OK);
	}
	
    @Operation(summary = "Delete an existing Category", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "204", description = "Category deleted"),
    		@ApiResponse(responseCode = "401", description = "Unauthorized access"),
    		@ApiResponse(responseCode = "404", description = "Unable to delete. Category not found")
    })
	@DeleteMapping("/categories/{id}")
	public ResponseEntity<Void> deleteCategory(@Parameter(description = "ID of Category to be deleted")
											   @PathVariable Long id) {

		carService.deleteCategoryAndAssociations(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
}
