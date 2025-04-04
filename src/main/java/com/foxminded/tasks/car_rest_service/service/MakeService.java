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

import com.foxminded.tasks.car_rest_service.dto.make.UpsertMakeDTO;
import com.foxminded.tasks.car_rest_service.dto.make.MakeDTO;
import com.foxminded.tasks.car_rest_service.entity.Make;
import com.foxminded.tasks.car_rest_service.mapper.MakeMapper;
import com.foxminded.tasks.car_rest_service.repository.MakeRepository;
import com.foxminded.tasks.car_rest_service.specification.MakeSpecification;

import jakarta.persistence.EntityNotFoundException;

@Service
public class MakeService {
	
	private final MakeRepository makeRepository;
	private final MakeMapper mapper;
	Logger logger = LoggerFactory.getLogger(MakeService.class);
	
	@Autowired
	public MakeService(MakeRepository makeRepository, MakeMapper makeMapper) {
		this.makeRepository = makeRepository;
		this.mapper = makeMapper;
	}

	public boolean existsByName(String name) {
		
		return makeRepository.existsByName(name);
	}
	
	public MakeDTO findByNameOrSaveNew(String name) {
		
		Make make = makeRepository.findByName(name)
				.orElseGet(() -> makeRepository.save(new Make(name)));
		
		return 	mapper.makeToDto(make);	
	}
	
	public MakeDTO findById(Long id) {
		
		Optional<Make> optMake = makeRepository.findById(id);
		
		if(!optMake.isEmpty()) {
			
			return mapper.makeToDto(optMake.get());
			
		} else {
			logger.error("Make with id {} is not found.", id);
			throw new EntityNotFoundException("ID: " + id + " Make is not found");
		}	
	}
	
	public MakeDTO createMake(UpsertMakeDTO createMakeDto) {
		
		if(!existsByName(createMakeDto.getName())) {

			Make newMake = makeRepository.save(new Make(createMakeDto.getName()));
			return mapper.makeToDto(newMake);
			
		} else {
			logger.error("Make with name {} is already exists.", createMakeDto.getName());
			throw new IllegalArgumentException("Make Name: " + createMakeDto.getName()
			 												 + " Make with this name is already exists");
		}	
	}
	
	public MakeDTO updateMake(Long id, UpsertMakeDTO updateMakeDto) {
		
		Optional<Make> optMakeToUpdate = makeRepository.findById(id);
		
		if(!optMakeToUpdate.isEmpty()) {
			
			Make makeToUpdate = optMakeToUpdate.get();
			makeToUpdate.setName(updateMakeDto.getName());
			Make updatedMake = makeRepository.save(makeToUpdate);
			
			return mapper.makeToDto(updatedMake);
			
		} else {
			logger.error("Make with id {} is not found.", id);
			throw new EntityNotFoundException("ID: " + id + " Make is not found");
		}
	}
	
	public void delete(Long id) {
		
		MakeDTO makeDto = findById(id);
		Make make = mapper.dtoToMake(makeDto);
		makeRepository.delete(make);
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
}
