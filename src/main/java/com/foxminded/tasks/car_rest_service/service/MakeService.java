package com.foxminded.tasks.car_rest_service.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.foxminded.tasks.car_rest_service.entity.Make;
import com.foxminded.tasks.car_rest_service.repository.MakeRepository;
import com.foxminded.tasks.car_rest_service.specification.MakeSpecification;

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

		Optional<Make> optMake = makeRepository.findById(id);

		if (optMake.isPresent()) {
			return optMake.get();

		} else {
			logger.error("Make with id {} is not found.", id);
			throw new EntityNotFoundException();
		}

	}
	
	public Make save(Make make) {

		if (!isMakeValid(make)) {
			logger.error("Save error. Make is not valid.");
			throw new IllegalArgumentException();

		} else {
			return makeRepository.save(make);
		}

	}
	
	public void delete(Make make) {

		if (!isMakeValid(make)) {
			logger.error("Delete error. Make is not valid.");
			throw new IllegalArgumentException();

		} else {
			makeRepository.delete(make);
		}

	}
	
	public Make findByNameOrSaveNew(String name) {
		
		return makeRepository.findByName(name)
				.orElseGet(() -> makeRepository.save(new Make(name)));		
	}
	
	public Page<Make> filterMakes(String name, Pageable pageable) {
		
		if (name == null || name.isEmpty()) {
			name = null;
		}
		
		Specification<Make> specification = Specification.where(MakeSpecification.filterByName(name));
		
		return makeRepository.findAll(specification, pageable);
	}
	
	private boolean isMakeValid(Make make) {
		
		return make != null &&
			   make.getName() != null;
	}

}
