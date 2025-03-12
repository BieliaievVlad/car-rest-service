package com.foxminded.tasks.car_rest_service.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.foxminded.tasks.car_rest_service.entity.Make;
import com.foxminded.tasks.car_rest_service.repository.MakeRepository;

@SpringBootTest
class MakeServiceTest {

	@Mock
	MakeRepository repository;
	
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
	void filterMakes__ValidValue_CalledMethodAndReturnsExpected() {
		
		String name = "Name";
		Pageable pageable = PageRequest.of(0, 10);
		Make make = new Make(1L, "Name");
		Page<Make> expected = new PageImpl<Make>(List.of(make));
		
		when(repository.findAll(any(Specification.class), eq(pageable))).thenReturn(expected);
		
		Page<Make> actual = service.filterMakes(name, pageable);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(repository, times(1)).findAll(any(Specification.class), eq(pageable));
	}

}
