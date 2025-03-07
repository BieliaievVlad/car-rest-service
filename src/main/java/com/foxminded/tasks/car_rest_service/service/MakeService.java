package com.foxminded.tasks.car_rest_service.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxminded.tasks.car_rest_service.entity.Make;
import com.foxminded.tasks.car_rest_service.repository.MakeRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class MakeService {
	
	private final MakeRepository makeRepository;
	Logger logger = LoggerFactory.getLogger(MakeService.class);
	
	@Autowired
	public MakeService(MakeRepository makeRepository) {
		this.makeRepository = makeRepository;
	}
	
	public List<Make> findAll() {
		return makeRepository.findAll();
	}
	
	public Make findById(Long id) {
		
		try {
			Optional<Make> optMake = makeRepository.findById(id);
			
			if(optMake.isPresent()) {
				return optMake.get();
				
			} else {
				throw new EntityNotFoundException();
			}
		} catch (Exception e) {
			logger.error("Make with id {} is not found.", id);
			return new Make();
		}
	}
	
	public void save(Make make) {
		
		try {
			
			if (!isMakeValid(make)) {
				throw new IllegalArgumentException();
				
			} else {
				makeRepository.save(make);
			}
		} catch (Exception e) {
			logger.error("Save error. Make is not valid.");
			logger.error("Name: {}", make.getName());
		}
	}
	
	public void delete(Make make) {
		
		try {
			
			if(!isMakeValid(make)) {
				throw new IllegalArgumentException();
				
			} else {
				makeRepository.delete(make);
			}
		} catch (Exception e) {
			logger.error("Delete error. Make is not valid.");
			logger.error("ID: {}", make.getId());
			logger.error("Name: {}", make.getName());
		}
	}
	
	private boolean isMakeValid(Make make) {
		
		return make != null &&
			   make.getName() != null;
	}

}
