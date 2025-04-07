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

import com.foxminded.tasks.car_rest_service.dto.model.UpsertModelDTO;
import com.foxminded.tasks.car_rest_service.dto.model.ModelDTO;
import com.foxminded.tasks.car_rest_service.service.CarService;
import com.foxminded.tasks.car_rest_service.service.ModelService;

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
public class ModelController {
	
	private final ModelService service;
	private CarService carService;
	
	@Autowired
	public ModelController(ModelService service,
						   CarService carService) {
		this.service = service;
		this.carService = carService;
	}
	
    @Operation(summary = "List Models with filtering options for name")
    @ApiResponses(value = { 
    		  @ApiResponse(responseCode = "200", description = "List of Models successfully fetched", 
    		    content = { @Content(mediaType = "application/json", 
    		      schema = @Schema(implementation = ModelDTO.class)) })
    		  })
	@GetMapping("/models")
	public Page<ModelDTO> getFilteredModels(@RequestParam(required = false) String name,
										    @RequestParam(defaultValue = "0") int page,
										    @RequestParam(defaultValue = "10") int size) {
		
    	Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
        return service.filterModels(name, pageable);
	}	
	
    @Operation(summary = "Find a Model with given id")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", description = "Model found", 
    					 content = { @Content(mediaType = "application/json",
    					 schema = @Schema(implementation = ModelDTO.class))
    		}),
    		@ApiResponse(responseCode = "404", description = "Model not found", content = @Content)
    })
	@GetMapping("/models/{id}")
	public ResponseEntity<ModelDTO> getModel(@Parameter(description = "ID of Model to be searched")
											 @PathVariable Long id) {

		ModelDTO modelDto = service.findById(id);
		return new ResponseEntity<>(modelDto, HttpStatus.OK);
	}
	
    @Operation(summary = "Create a new Model", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "201", description = "Model created and added to DataBase",
    					 content = { @Content(mediaType = "application/json",
    					 schema = @Schema(implementation = ModelDTO.class))
    					 }),
    		@ApiResponse(responseCode = "400", description = "Model with given name is already exists"),
    		@ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
	@PostMapping("/models")
	public ResponseEntity<ModelDTO> createModel(@io.swagger.v3.oas.annotations.parameters.RequestBody(
	  													description = "Model to create", required = true, 
	  													content = @Content(mediaType = "application/json", 
	  													schema = @Schema(implementation = UpsertModelDTO.class),
	  													examples = @ExampleObject(value = "{\"name\": \"KALINA\"}")))
												@RequestBody UpsertModelDTO createModelDto) {

		ModelDTO modelDto = service.createModel(createModelDto);
		return new ResponseEntity<>(modelDto, HttpStatus.CREATED);
	}
	
    @Operation(summary = "Update an existing Model", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", description = "Model updated",
    				content = { @Content(mediaType = "application/json",
    				schema = @Schema(implementation = ModelDTO.class))
    				}),
    		@ApiResponse(responseCode = "400", description = "Model data is not valid"),
    		@ApiResponse(responseCode = "401", description = "Unauthorized access"),
    		@ApiResponse(responseCode = "404", description = "Unable to update. Model not found", content = @Content)
    })
	@PutMapping("/models/{id}")
	public ResponseEntity<ModelDTO> updateModel(@Parameter(description = "ID of Model to be updated")
												@PathVariable Long id, 
												@io.swagger.v3.oas.annotations.parameters.RequestBody(
	  													description = "Model data to update", required = true, 
	  													content = @Content(mediaType = "application/json", 
	  													schema = @Schema(implementation = UpsertModelDTO.class),
	  													examples = @ExampleObject(value = "{\"name\": \"KALINA\"}")))
												@RequestBody UpsertModelDTO updateModelDto) {

		ModelDTO modelDto = service.updateModel(id, updateModelDto);
		return new ResponseEntity<>(modelDto, HttpStatus.OK);
	}
	
    @Operation(summary = "Delete an existing Model", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "204", description = "Model deleted"),
    		@ApiResponse(responseCode = "401", description = "Unauthorized access"),
    		@ApiResponse(responseCode = "404", description = "Unable to delete. Model not found")
    })
	@DeleteMapping("/models/{id}")
	public ResponseEntity<Void> deleteModel(@Parameter(description = "ID of Model to be deleted")
											@PathVariable Long id) {

		carService.deleteModelAndAssociations(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
