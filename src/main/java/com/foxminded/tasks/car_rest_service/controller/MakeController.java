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

import com.foxminded.tasks.car_rest_service.entity.Make;
import com.foxminded.tasks.car_rest_service.service.MakeService;
import com.foxminded.tasks.car_rest_service.service.PersistenceService;

@RestController
@RequestMapping("api/v1")
public class MakeController {
	
	private final MakeService makeService;
	private final PersistenceService service;
	
	@Autowired
	public MakeController(MakeService makeService,PersistenceService service) {
		this.makeService = makeService;
		this.service = service;
	}
	
	@GetMapping("/makes")
	public Page<Make> getFilteredMakes(@RequestParam(required = false) String name,
									   @RequestParam(defaultValue = "0") int page,
									   @RequestParam(defaultValue = "10") int size) {
		
    	Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
        return makeService.filterMakes(name, pageable);
	}

}
