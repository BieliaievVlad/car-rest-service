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

import com.foxminded.tasks.car_rest_service.dto.make.CreateUpdateMakeDTO;
import com.foxminded.tasks.car_rest_service.dto.make.MakeDTO;
import com.foxminded.tasks.car_rest_service.entity.Car;
import com.foxminded.tasks.car_rest_service.entity.Make;
import com.foxminded.tasks.car_rest_service.mapper.MakeMapper;
import com.foxminded.tasks.car_rest_service.repository.MakeRepository;
import com.foxminded.tasks.car_rest_service.specification.MakeSpecification;

import jakarta.persistence.EntityNotFoundException;

@Service
public class MakeService {
	
	private final MakeRepository makeRepository;
	private CarService carService;
	private final MakeMapper mapper;
	Logger logger = LoggerFactory.getLogger(MakeService.class);
	
	@Autowired
	public MakeService(MakeRepository makeRepository, CarService carService, MakeMapper makeMapper) {
		this.makeRepository = makeRepository;
		this.carService = carService;
		this.mapper = makeMapper;
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
	
	public boolean existsByName(String name) {
		
		return makeRepository.existsByName(name);
	}
	
	public Make findByNameOrSaveNew(String name) {
		
		return makeRepository.findByName(name)
				.orElseGet(() -> makeRepository.save(new Make(name)));		
	}
	
	public MakeDTO findMakeById(Long id) {
		
		Make make = findById(id);
		
		return mapper.makeToDto(make);
	}
	
	public MakeDTO createMake(CreateUpdateMakeDTO createMakeDto) {
		
		if(!existsByName(createMakeDto.getName())) {

			Make newMake = save(new Make(createMakeDto.getName()));
			return mapper.makeToDto(newMake);
			
		} else {
			logger.error("Make with name {} is already exists.", createMakeDto.getName());
			throw new IllegalArgumentException();
		}	
	}
	
	public MakeDTO updateMake(Long id, CreateUpdateMakeDTO updateMakeDto) {
		
		Make makeToUpdate = findById(id);
		makeToUpdate.setName(updateMakeDto.getName());
		Make updatedMake = save(makeToUpdate);
		
		return mapper.makeToDto(updatedMake);
	}
	
	public void deleteMakeAndAssociations(Long id) {
		
		Make make = findById(id);
		List<Car> cars = carService.findByMake(make);
		
		for (Car c : cars) {
			carService.delete(c);
		}
		
		delete(make);
	}
	
	public Page<MakeDTO> filterMakes(String name, Pageable pageable) {
		
		if (name == null || name.isEmpty()) {
			name = null;
		}
		
		Specification<Make> specification = Specification.where(MakeSpecification.filterByName(name));
		
		Page<Make> makesPage = makeRepository.findAll(specification, pageable);
		
		List<MakeDTO> makesDto = makesPage.getContent().stream()
				.map(mapper::makeToDto)
				.collect(Collectors.toList());
			
		return new PageImpl<>(makesDto, pageable, makesPage.getTotalElements());
	}
	
	private boolean isMakeValid(Make make) {
		
		return make != null &&
			   make.getName() != null;
	}
}
