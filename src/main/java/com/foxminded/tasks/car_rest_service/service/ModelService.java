package com.foxminded.tasks.car_rest_service.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.foxminded.tasks.car_rest_service.dto.ModelDTO;
import com.foxminded.tasks.car_rest_service.entity.Model;
import com.foxminded.tasks.car_rest_service.mapper.ModelMapper;
import com.foxminded.tasks.car_rest_service.repository.ModelRepository;
import com.foxminded.tasks.car_rest_service.specification.ModelSpecification;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ModelService {
	
	private final ModelRepository modelRepository;
	private final ModelMapper mapper;
	Logger logger = LoggerFactory.getLogger(ModelService.class);
	
	@Autowired
	public ModelService(ModelRepository modelRepository, ModelMapper mapper) {
		this.modelRepository = modelRepository;
		this.mapper = mapper;
	}
	
	public List<Model> findAll() {
		
		return modelRepository.findAll();
	}
	
	public Model findById(Long id) {

		Optional<Model> optModel = modelRepository.findById(id);

		if (optModel.isPresent()) {
			return optModel.get();

		} else {
			logger.error("Model with id {} is not found.", id);
			throw new EntityNotFoundException();
		}

	}
	
	public Model save(Model model) {
		
		if (!isModelValid(model)) {
			logger.error("Save error. Model is not valid.");
			throw new IllegalArgumentException();

		} else {
			return modelRepository.save(model);
		}

	}
	
	public void delete(Model model) {
		
		if (!isModelValid(model)) {
			logger.error("Delete error. Model is not valid.");
			throw new IllegalArgumentException();

		} else {
			modelRepository.delete(model);
		}

	}
	
	public boolean existsByName(String name) {
		
		return modelRepository.existsByName(name);
	}
	
	public Model findByNameOrSaveNew(String name) {
		
		return modelRepository.findByName(name)
				.orElseGet(() -> modelRepository.save(new Model(name)));
	}
	
	public Page<ModelDTO> filterModels (String name, Pageable pageable) {
		
		if (name == null || name.isEmpty()) {
			name = null;
		}
		
		Specification<Model> specification = Specification.where(ModelSpecification.filterByName(name));
		
		Page<Model> modelsPage = modelRepository.findAll(specification, pageable);
		
		List<ModelDTO> modelsDto = modelsPage.getContent().stream()
				.map(mapper :: modelToDto)
				.collect(Collectors.toList());
		
		return new PageImpl<>(modelsDto, pageable, modelsPage.getTotalElements());
	}
	
	private boolean isModelValid(Model model) {
		
		return model != null &&
			   model.getName() != null;
	}

}
