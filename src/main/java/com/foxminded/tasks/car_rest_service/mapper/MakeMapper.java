package com.foxminded.tasks.car_rest_service.mapper;

import org.springframework.stereotype.Component;

import com.foxminded.tasks.car_rest_service.dto.MakeDTO;
import com.foxminded.tasks.car_rest_service.entity.Make;

@Component
public class MakeMapper {
	
	public MakeDTO makeToDto(Make make) {
		
		return new MakeDTO(make.getId(), make.getName());
	}
	
	public Make dtoToMake(MakeDTO makeDto) {
		
		return new Make(makeDto.getId(), makeDto.getName());
	}
}
