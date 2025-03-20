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

import com.foxminded.tasks.car_rest_service.dto.make.CreateUpdateMakeDTO;
import com.foxminded.tasks.car_rest_service.dto.make.MakeDTO;
import com.foxminded.tasks.car_rest_service.service.MakeService;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("api/v1")
public class MakeController {
	
	private final MakeService service;
	
	@Autowired
	public MakeController(MakeService service) {
		this.service = service;
	}
	
	@GetMapping("/makes")
	public Page<MakeDTO> getFilteredMakes(@RequestParam(required = false) String name,
									   	  @RequestParam(defaultValue = "0") int page,
									   	  @RequestParam(defaultValue = "10") int size) {
		
    	Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
        return service.filterMakes(name, pageable);
	}
	
	@GetMapping("/makes/{id}")
	public ResponseEntity<MakeDTO> getMake(@PathVariable Long id) {
		
		try {
			MakeDTO makeDto = service.findMakeById(id);
			return new ResponseEntity<>(makeDto, HttpStatus.OK);
			
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/makes")
	public ResponseEntity<MakeDTO> createMake(@RequestBody CreateUpdateMakeDTO createMakeDto) {

		try {
			MakeDTO makeDto = service.createMake(createMakeDto);
			return new ResponseEntity<>(makeDto, HttpStatus.CREATED);
			
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/makes/{id}")
	public ResponseEntity<MakeDTO> updateMake(@PathVariable Long id, @RequestBody CreateUpdateMakeDTO updateMakeDto) {
		
		try {
			MakeDTO makeDto = service.updateMake(id, updateMakeDto);
			return new ResponseEntity<>(makeDto, HttpStatus.OK);
			
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/makes/{id}")
	public ResponseEntity<Void> deleteMake(@PathVariable Long id) {

		try {
			service.deleteMakeAndAssociations(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

