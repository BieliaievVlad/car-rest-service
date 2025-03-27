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
import jakarta.persistence.EntityNotFoundException;

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
	
	@GetMapping("/models")
	public Page<ModelDTO> getFilteredModels(@RequestParam(required = false) String name,
										    @RequestParam(defaultValue = "0") int page,
										    @RequestParam(defaultValue = "10") int size) {
		
    	Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
        return service.filterModels(name, pageable);
	}	
	
	@GetMapping("/models/{id}")
	public ResponseEntity<ModelDTO> getModel(@PathVariable Long id) {
		
		try {
			ModelDTO modelDto = service.findById(id);
			return new ResponseEntity<>(modelDto, HttpStatus.OK);
			
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/models")
	public ResponseEntity<ModelDTO> createModel(@RequestBody UpsertModelDTO createModelDto) {

		try {
			ModelDTO modelDto = service.createModel(createModelDto);
			return new ResponseEntity<>(modelDto, HttpStatus.CREATED);
			
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/models/{id}")
	public ResponseEntity<ModelDTO> updateModel(@PathVariable Long id, @RequestBody UpsertModelDTO updateModelDto) {
		
		try {
			ModelDTO modelDto = service.updateModel(id, updateModelDto);
			return new ResponseEntity<>(modelDto, HttpStatus.OK);
			
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/models/{id}")
	public ResponseEntity<Void> deleteModel(@PathVariable Long id) {

		try {
			carService.deleteModelAndAssociations(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
