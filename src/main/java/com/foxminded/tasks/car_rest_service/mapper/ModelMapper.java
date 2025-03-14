package com.foxminded.tasks.car_rest_service.mapper;

import org.springframework.stereotype.Component;

import com.foxminded.tasks.car_rest_service.dto.ModelDTO;
import com.foxminded.tasks.car_rest_service.entity.Model;

@Component
public class ModelMapper {

	public ModelDTO modelToDto(Model model) {
		
		return new ModelDTO(model.getId(), model.getName());
	}
	
	public Model dtoToModel(ModelDTO modelDto) {
		
		return new Model(modelDto.getId(), modelDto.getName());
	}
}
