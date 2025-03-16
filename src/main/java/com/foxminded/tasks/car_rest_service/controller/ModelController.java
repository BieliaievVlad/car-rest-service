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

import com.foxminded.tasks.car_rest_service.dto.ModelDTO;
import com.foxminded.tasks.car_rest_service.service.ModelService;
import com.foxminded.tasks.car_rest_service.service.DataManagementService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("api/v1")
public class ModelController {
	
	private final ModelService modelService;
	private final DataManagementService service;
	
	@Autowired
	public ModelController(ModelService modelService, DataManagementService service) {
		this.modelService = modelService;
		this.service = service;
	}
	
	@GetMapping("/models")
	public Page<ModelDTO> getFilteredModels(@RequestParam(required = false) String name,
										    @RequestParam(defaultValue = "0") int page,
										    @RequestParam(defaultValue = "10") int size) {
		
    	Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
        return modelService.filterModels(name, pageable);
	}	
	
	@GetMapping("/models/{id}")
	public ResponseEntity<ModelDTO> getModel(@PathVariable Long id) {
		
		try {
			ModelDTO modelDto = service.findModelById(id);
			return new ResponseEntity<>(modelDto, HttpStatus.OK);
			
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/models")
	public ResponseEntity<ModelDTO> createModel(@RequestBody ModelDTO modelDto) {

		try {
			service.createModel(modelDto);
			return new ResponseEntity<>(modelDto, HttpStatus.CREATED);
			
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/models/{id}")
	public ResponseEntity<Void> deleteModel(@PathVariable Long id) {

		try {
			service.deleteModelAndAssociations(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
