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

import com.foxminded.tasks.car_rest_service.dto.model.UpsertModelDTO;
import com.foxminded.tasks.car_rest_service.dto.model.ModelDTO;
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

	public boolean existsByName(String name) {
		
		return modelRepository.existsByName(name);
	}
	
	public ModelDTO findByNameOrSaveNew(String name) {
		
		Model model = modelRepository.findByName(name)
				.orElseGet(() -> modelRepository.save(new Model(name)));
		
		return mapper.modelToDto(model);
	}
	
	public ModelDTO findById(Long id) {
		
		Optional<Model> optModel = modelRepository.findById(id);
		
		if(!optModel.isEmpty()) {
			
			return mapper.modelToDto(optModel.get());
			
		} else {
			logger.error("Model with id {} is not found.", id);
			throw new EntityNotFoundException();
		}	
	}
	
	public ModelDTO createModel(UpsertModelDTO createModelDto) {
		
		if(!existsByName(createModelDto.getName())) {
			
			Model newModel = modelRepository.save(new Model(createModelDto.getName()));
			return mapper.modelToDto(newModel);
			
		} else {
			logger.error("Model with name {} is already exists.", createModelDto.getName());
			throw new IllegalArgumentException();
		}	
	}
	
	public ModelDTO updateModel(Long id, UpsertModelDTO updateModelDto) {
		
		Optional<Model> optModelToUpdate = modelRepository.findById(id);
		
		if(!optModelToUpdate.isEmpty()) {
			
			Model modelToUpdate = optModelToUpdate.get();
			modelToUpdate.setName(updateModelDto.getName());
			Model updatedModel = modelRepository.save(modelToUpdate);
			
			return mapper.modelToDto(updatedModel);
			
		} else {
			logger.error("Model with id {} is not found.", id);
			throw new EntityNotFoundException();
		}
	}
	
	public void delete(Long id) {
		
		ModelDTO modelDto = findById(id);
		Model model = mapper.dtoToModel(modelDto);
		modelRepository.delete(model);
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
}
