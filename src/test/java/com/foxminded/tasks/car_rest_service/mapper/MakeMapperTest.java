package com.foxminded.tasks.car_rest_service.mapper;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import com.foxminded.tasks.car_rest_service.dto.MakeDTO;
import com.foxminded.tasks.car_rest_service.entity.Make;

@SpringBootTest
class MakeMapperTest {

	@InjectMocks
	MakeMapper mapper;
	
	@Test
	void makeToDto_ValidMake_ReturnsExpected() {
		
		Make make = new Make(1L, "Name");
		MakeDTO expected = new MakeDTO(1L, "Name");
		
		MakeDTO actual = mapper.makeToDto(make);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
	}

	@Test
	void dtoToMake_ValidDto_ReturnsExpected() {
		
		Make expected = new Make(1L, "Name");
		MakeDTO makeDto = new MakeDTO(1L, "Name");
		
		Make actual = mapper.dtoToMake(makeDto);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
	}

}
