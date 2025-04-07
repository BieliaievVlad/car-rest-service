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

import com.foxminded.tasks.car_rest_service.dto.car.CarDTO;
import com.foxminded.tasks.car_rest_service.dto.car.CarListItemDTO;
import com.foxminded.tasks.car_rest_service.dto.car.CreateCarDTO;
import com.foxminded.tasks.car_rest_service.dto.car.UpdateCarDTO;
import com.foxminded.tasks.car_rest_service.service.CarService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement; 

@RestController
@RequestMapping("/api/v1")
public class CarController {

    private final CarService service;

    @Autowired
    public CarController(CarService service) {
        this.service = service;
    }

    @Operation(summary = "List Cars with filtering options for make, model, category, and year")
    @ApiResponses(value = { 
    		  @ApiResponse(responseCode = "200", description = "List of Cars successfully fetched", 
    		    content = { @Content(mediaType = "application/json", 
    		      schema = @Schema(implementation = CarListItemDTO.class)) })
    		  })
    @GetMapping("/cars")
    public Page<CarListItemDTO> getFilteredCars(
            @RequestParam(required = false) String makeName,
            @RequestParam(required = false) String modelName,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) Integer year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
    	Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
        return service.filterCars(makeName, modelName, categoryName, year, pageable);
    }
    
    @Operation(summary = "Find a Car with given id")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", description = "Car found", 
    					 content = { @Content(mediaType = "application/json",
    					 schema = @Schema(implementation = CarDTO.class))
    		}),
    		@ApiResponse(responseCode = "404", description = "Car not found", content = @Content)
    })
    @GetMapping("/cars/{id}")
    public ResponseEntity<CarDTO> getCar(@Parameter(description = "ID of Car to be searched")
    									 @PathVariable Long id) {

    	CarDTO carDto = service.findCarById(id);
    	return new ResponseEntity<>(carDto, HttpStatus.OK);
    }
    
    @Operation(summary = "Create a new Car", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "201", description = "Car created and added to DataBase",
    					 content = { @Content(mediaType = "application/json",
    					 schema = @Schema(implementation = CarDTO.class))
    					 }),
    		@ApiResponse(responseCode = "400", description = "Car data is not valid"),
    		@ApiResponse(responseCode = "401", description = "Unauthorized access"),
    })
	@PostMapping("/cars")
	public ResponseEntity<CarDTO> createCar(@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "Car to create", required = true, 
			content = @Content(mediaType = "application/json", 
			schema = @Schema(implementation = CreateCarDTO.class),
			examples = @ExampleObject(value = 
					"{\"make\": \"LADA\",\"model\": \"KALINA\",\"category\": \"Sedan\",\"year\": \"2026\",\"objectId\": \"\"}")))
			@RequestBody CreateCarDTO createCarDto) {

		CarDTO carDto = service.createCar(createCarDto);
		return new ResponseEntity<>(carDto, HttpStatus.CREATED);
	}
	
    @Operation(summary = "Update an existing Car", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", description = "Car updated",
    				content = { @Content(mediaType = "application/json",
    				schema = @Schema(implementation = CarDTO.class))
    				}),
    		@ApiResponse(responseCode = "400", description = "Car data is not valid"),
    		@ApiResponse(responseCode = "401", description = "Unauthorized access"),
    		@ApiResponse(responseCode = "404", description = "Unable to update. Car not found", content = @Content)
    })
	@PutMapping("/cars/{id}")
	public ResponseEntity<CarDTO> updateCar(@Parameter(description = "ID of Car to be updated") @PathVariable Long id, 
			@io.swagger.v3.oas.annotations.parameters.RequestBody(
				description = "Car data to update", required = true, 
				content = @Content(mediaType = "application/json", 
				schema = @Schema(implementation = UpdateCarDTO.class),
				examples = @ExampleObject(value = 
					"{\"make\": \"LADA\",\"model\": \"KALINA\",\"category\": \"Sedan\",\"year\": \"2026\"}")))
				@RequestBody UpdateCarDTO updateCarDto) {

		CarDTO carDto = service.updateCar(id, updateCarDto);
		return new ResponseEntity<>(carDto, HttpStatus.OK);
	}
	
    @Operation(summary = "Delete an existing Car", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "204", description = "Car deleted"),
    		@ApiResponse(responseCode = "401", description = "Unauthorized access"),
    		@ApiResponse(responseCode = "404", description = "Unable to delete. Car not found")
    })
	@DeleteMapping("/cars/{id}")
	public ResponseEntity<Void> deleteCar(@Parameter(description = "ID of Car to be deleted")
										  @PathVariable Long id) {

		service.delete(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
