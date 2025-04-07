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

import com.foxminded.tasks.car_rest_service.dto.make.UpsertMakeDTO;
import com.foxminded.tasks.car_rest_service.dto.make.MakeDTO;
import com.foxminded.tasks.car_rest_service.service.CarService;
import com.foxminded.tasks.car_rest_service.service.MakeService;

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
public class MakeController {
	
	private final MakeService service;
	private CarService carService;
	
	@Autowired
	public MakeController(MakeService service,
						  CarService carService) {
		this.service = service;
		this.carService = carService;
	}
	
    @Operation(summary = "List Makes with filtering options for name")
    @ApiResponses(value = { 
    		  @ApiResponse(responseCode = "200", description = "List of Makes successfully fetched", 
    		    content = { @Content(mediaType = "application/json", 
    		      schema = @Schema(implementation = MakeDTO.class)) })
    		  })
	@GetMapping("/makes")
	public Page<MakeDTO> getFilteredMakes(@RequestParam(required = false) String name,
									   	  @RequestParam(defaultValue = "0") int page,
									   	  @RequestParam(defaultValue = "10") int size) {
		
    	Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
        return service.filterMakes(name, pageable);
	}
	
    @Operation(summary = "Find a Make with given id")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", description = "Make found", 
    					 content = { @Content(mediaType = "application/json",
    					 schema = @Schema(implementation = MakeDTO.class))
    		}),
    		@ApiResponse(responseCode = "404", description = "Make not found", content = @Content)
    })
	@GetMapping("/makes/{id}")
	public ResponseEntity<MakeDTO> getMake(@Parameter(description = "ID of Make to be searched")
										   @PathVariable Long id) {

		MakeDTO makeDto = service.findById(id);
		return new ResponseEntity<>(makeDto, HttpStatus.OK);
	}
	
    @Operation(summary = "Create a new Make", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "201", description = "Make created and added to DataBase",
    					 content = { @Content(mediaType = "application/json",
    					 schema = @Schema(implementation = MakeDTO.class))
    					 }),
    		@ApiResponse(responseCode = "400", description = "Make with given name is already exists"),
    		@ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
	@PostMapping("/makes")
	public ResponseEntity<MakeDTO> createMake(@io.swagger.v3.oas.annotations.parameters.RequestBody(
											  		description = "Make to create", required = true, 
											  		content = @Content(mediaType = "application/json", 
											  		schema = @Schema(implementation = UpsertMakeDTO.class),
											  		examples = @ExampleObject(value = "{\"name\": \"LADA\"}")))
											  @RequestBody UpsertMakeDTO createMakeDto) {

		MakeDTO makeDto = service.createMake(createMakeDto);
		return new ResponseEntity<>(makeDto, HttpStatus.CREATED);
	}
	
    @Operation(summary = "Update an existing Make", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", description = "Make updated",
    				content = { @Content(mediaType = "application/json",
    				schema = @Schema(implementation = MakeDTO.class))
    				}),
    		@ApiResponse(responseCode = "400", description = "Make data is not valid"),
    		@ApiResponse(responseCode = "401", description = "Unauthorized access"),
    		@ApiResponse(responseCode = "404", description = "Unable to update. Make not found", content = @Content)
    })
	@PutMapping("/makes/{id}")
	public ResponseEntity<MakeDTO> updateMake(@Parameter(description = "ID of Make to be updated")
											  @PathVariable Long id, 
											  @io.swagger.v3.oas.annotations.parameters.RequestBody(
														description = "Make data to update", required = true, 
														content = @Content(mediaType = "application/json", 
														schema = @Schema(implementation = UpsertMakeDTO.class),
														examples = @ExampleObject(value = "{\"name\": \"LADA\"}")))
											  @RequestBody UpsertMakeDTO updateMakeDto) {

		MakeDTO makeDto = service.updateMake(id, updateMakeDto);
		return new ResponseEntity<>(makeDto, HttpStatus.OK);
	}
	
    @Operation(summary = "Delete an existing Make", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "204", description = "Make deleted"),
    		@ApiResponse(responseCode = "401", description = "Unauthorized access"),
    		@ApiResponse(responseCode = "404", description = "Unable to delete. Make not found")
    })
	@DeleteMapping("/makes/{id}")
	public ResponseEntity<Void> deleteMake(@Parameter(description = "ID of Make to be deleted")
										   @PathVariable Long id) {

		carService.deleteMakeAndAssociations(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}

