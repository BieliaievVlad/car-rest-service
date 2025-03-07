package com.foxminded.tasks.car_rest_service.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxminded.tasks.car_rest_service.entity.Model;
import com.foxminded.tasks.car_rest_service.repository.ModelRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ModelService {
	
	private final ModelRepository modelRepository;
	Logger logger = LoggerFactory.getLogger(ModelService.class);
	
	@Autowired
	public ModelService(ModelRepository modelRepository) {
		this.modelRepository = modelRepository;
	}
	
	public List<Model> findAll() {
		return modelRepository.findAll();
	}
	
	public Model findById(Long id) {
		
		try {
			
			Optional<Model> optModel = modelRepository.findById(id);
			
			if(optModel.isPresent()) {
				return optModel.get();
				
			} else {
				throw new EntityNotFoundException();
			}
		} catch (Exception e) {
			logger.error("Model with id {} is not found.", id);
			return new Model();
		}
	}
	
	public void save(Model model) {
		
		try {
			
			if(!isModelValid(model)) {
				throw new IllegalArgumentException();
				
			} else {
				modelRepository.save(model);
			}
		} catch (Exception e) {
			logger.error("Save error. Model is not valid.");
			logger.error("Name: {}", model.getName());
		}
	}
	
	public void delete(Model model) {
		
		try {
			
			if(!isModelValid(model)) {
				throw new IllegalArgumentException();
				
			} else {
				modelRepository.delete(model);
			}
		} catch (Exception e) {
			logger.error("Delete error. Model is not valid.");
			logger.error("ID: {}", model.getId());
			logger.error("Name: {}", model.getName());
		}
	}
	
	private boolean isModelValid(Model model) {
		
		return model != null &&
			   model.getName() != null;
	}

}
