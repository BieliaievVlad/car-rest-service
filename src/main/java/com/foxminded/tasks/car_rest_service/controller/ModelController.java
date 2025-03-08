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

import com.foxminded.tasks.car_rest_service.entity.Model;
import com.foxminded.tasks.car_rest_service.service.ModelService;
import com.foxminded.tasks.car_rest_service.service.PersistenceService;

@RestController
@RequestMapping("api/v1")
public class ModelController {
	
	private final ModelService modelService;
	private final PersistenceService service;
	
	@Autowired
	public ModelController(ModelService modelService, PersistenceService service) {
		this.modelService = modelService;
		this.service = service;
	}
	
	@GetMapping("/models")
	public Page<Model> getFilteredModels(@RequestParam(required = false) String name,
										 @RequestParam(defaultValue = "0") int page,
										 @RequestParam(defaultValue = "10") int size) {
		
    	Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
        return modelService.filterModels(name, pageable);
	}		

}
