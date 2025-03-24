package com.foxminded.tasks.car_rest_service.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import com.foxminded.tasks.car_rest_service.dto.make.MakeDTO;
import com.foxminded.tasks.car_rest_service.dto.make.UpsertMakeDTO;
import com.foxminded.tasks.car_rest_service.entity.Make;
import com.foxminded.tasks.car_rest_service.mapper.MakeMapper;
import com.foxminded.tasks.car_rest_service.repository.MakeRepository;

@ExtendWith(MockitoExtension.class)
class MakeServiceTest {

	@Mock
	MakeRepository repository;
	
	@Mock
	MakeMapper mapper;
	
	@InjectMocks
	MakeService service;
	
	@Test
	void findAll__ValidValue_CalledMethodAndReturnsExpected() {
		
		List<Make> expected = List.of(new Make());
		
		when(repository.findAll()).thenReturn(expected);
		
		List<Make> actual = service.findAll();
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(repository, times(1)).findAll();
		}

	@Test
	void findById__ValidValue_CalledMethodAndReturnsExpected() {
		
		Long id = 1L;
		Make expected = new Make(1L, "Name");
		
		when(repository.findById(anyLong())).thenReturn(Optional.of(expected));
		
		Make actual = service.findById(id);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(repository, times(1)).findById(anyLong());
	}

	@Test
	void save__ValidValue_CalledMethodAndReturnsExpected() {
		
		Make expected = new Make(1L, "Name");
		
		when(repository.save(any(Make.class))).thenReturn(expected);
		
		Make actual = service.save(expected);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(repository, times(1)).save(any(Make.class));
		}

	@Test
	void delete__ValidValue_CalledMethod() {
		
		Make make = new Make(1L, "Name");
		
		doNothing().when(repository).delete(any(Make.class));
		
		service.delete(make);
		
		verify(repository, times(1)).delete(any(Make.class));
	}

	@Test
	void existsByName__ValidValue_CalledMethodAndReturnsExpected() {
		
		String name = "Name";
		
		when(repository.existsByName(anyString())).thenReturn(true);
		
		boolean result = service.existsByName(name);
		
		assertThat(result).isTrue();
		verify(repository, times(1)).existsByName(anyString());
	}

	@Test
	void findByNameOrSaveNew__MakeExists_CalledMethodAndReturnsExpected() {
		
		String name = "Name";
		Make expected = new Make(1L, "Name");
		
		when(repository.findByName(anyString())).thenReturn(Optional.of(expected));
		
		Make actual = service.findByNameOrSaveNew(name);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(repository, times(1)).findByName(anyString());
	}
	
	@Test
	void findByNameOrSaveNew__MakeNotExists_CalledMethodAndReturnsExpected() {
		
		String name = "Name";
		Make expected = new Make(1L, "Name");
		
		when(repository.findByName(anyString())).thenReturn(Optional.empty());
		when(repository.save(any(Make.class))).thenReturn(expected);
		
		Make actual = service.findByNameOrSaveNew(name);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(repository, times(1)).findByName(anyString());
		verify(repository, times(1)).save(any(Make.class));
	}
	
	@Test
	void findMakeById_ValidId_CalledMethodsAndReturnsExpected() {
		
		long id = 1L;
		Make make = new Make(1L, "Name");
		MakeDTO expected = new MakeDTO(1L, "Name");
		
		when(repository.findById(anyLong())).thenReturn(Optional.of(make));
		when(mapper.makeToDto(any(Make.class))).thenReturn(expected);
		
		MakeDTO actual = service.findMakeById(id);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(repository, times(1)).findById(anyLong());
		verify(mapper, times(1)).makeToDto(any(Make.class));
	}
	
	@Test
	void createMake_ValidValue_CalledMethodAndReturnsExpected() {
		
		Make make = new Make(1L, "Name");
		UpsertMakeDTO createDto = new UpsertMakeDTO("Name");
		MakeDTO expected = new MakeDTO(1L, "Name");
		
		when(repository.existsByName(anyString())).thenReturn(false);
		when(repository.save(any(Make.class))).thenReturn(make);
		when(mapper.makeToDto(any(Make.class))).thenReturn(expected);
		
		MakeDTO actual = service.createMake(createDto);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(repository, times(1)).existsByName(anyString());
		verify(repository, times(1)).save(any(Make.class));
		verify(mapper, times(1)).makeToDto(any(Make.class));
	}
	
	@Test
	void createMake_InvalidValue_CalledMethodAndReturnsExpected() {

		UpsertMakeDTO makeDto = new UpsertMakeDTO("Name");
		
		when(repository.existsByName(anyString())).thenReturn(true);
		
		assertThrows(IllegalArgumentException.class, () -> {
			service.createMake(makeDto);
		});
		verify(repository, times(1)).existsByName(anyString());
	}
	
	@Test
	void updateMake_ValidMake_CalledMethodsAndReturnsExpected() {
		
		Long id = 1L;
		Make make = new Make(1L, "Name");
		UpsertMakeDTO updateDto = new UpsertMakeDTO("Name");
		MakeDTO expected = new MakeDTO(1L, "Make_Name");
		
		when(repository.findById(anyLong())).thenReturn(Optional.of(make));
		when(repository.save(any(Make.class))).thenReturn(make);
		when(mapper.makeToDto(any(Make.class))).thenReturn(expected);
		
		MakeDTO actual = service.updateMake(id, updateDto);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(repository, times(1)).findById(anyLong());
		verify(repository, times(1)).save(any(Make.class));
		verify(mapper, times(1)).makeToDto(any(Make.class));
	}

	@Test
	void filterMakes__ValidValue_CalledMethodAndReturnsExpected() {
		
		String name = "Name";
		Pageable pageable = PageRequest.of(0, 10);
		Make make = new Make(1L, "Name");
		MakeDTO makeDto = new MakeDTO(1L, "Name");
		Page<Make> page = new PageImpl<Make>(List.of(make));
		Page<MakeDTO> expected = new PageImpl<MakeDTO>(List.of(makeDto));
		
		when(repository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
		when(mapper.makeToDto(any(Make.class))).thenReturn(makeDto);
		
		Page<MakeDTO> actual = service.filterMakes(name, pageable);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(repository, times(1)).findAll(any(Specification.class), eq(pageable));
		verify(mapper, times(1)).makeToDto(any(Make.class));
	}

}
