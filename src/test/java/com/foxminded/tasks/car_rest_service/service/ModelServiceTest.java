package com.foxminded.tasks.car_rest_service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
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

import com.foxminded.tasks.car_rest_service.entity.Model;
import com.foxminded.tasks.car_rest_service.repository.ModelRepository;

@SpringBootTest
class ModelServiceTest {
	
	@Mock
	ModelRepository repository;
	
	@InjectMocks
	ModelService service;

	@Test
	void findAll_ValidValue_CalledMethodAndReturnsExpected() {
		
		List<Model> expected = List.of(new Model(1L, "Name"));
		
		when(repository.findAll()).thenReturn(expected);
		
		List<Model> actual = service.findAll();
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(repository, times(1)).findAll();
	}

	@Test
	void findById_ValidValue_CalledMethodAndReturnsExpected() {
		
		Long id = 1L;
		Model expected = new Model(1L, "Name");
		
		when(repository.findById(anyLong())).thenReturn(Optional.of(expected));
		
		Model actual = service.findById(id);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(repository, times(1)).findById(anyLong());
	}

	@Test
	void save_ValidValue_CalledMethodAndReturnsExpected() {
		
		Model expected = new Model(1L, "Name");
		
		when(repository.save(any(Model.class))).thenReturn(expected);
		
		Model actual = service.save(expected);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(repository, times(1)).save(any(Model.class));
	}

	@Test
	void delete_ValidValue_CalledMethod() {
		
		Model model = new Model(1L, "Name");
		
		doNothing().when(repository).delete(any(Model.class));
		
		service.delete(model);
		
		verify(repository, times(1)).delete(any(Model.class));
	}

	@Test
	void existsByName_ValidValue_CalledMethodAndReturnsExpected() {
		
		String name = "Name";
		
		when(repository.existsByName(anyString())).thenReturn(true);
		
		boolean result = repository.existsByName(name);
		
		assertThat(result).isTrue();
		verify(repository, times(1)).existsByName(anyString());
	}

	@Test
	void findByNameOrSaveNew_ModelExists_CalledMethodAndReturnsExpected() {
		
		String name = "Name";
		Model expected = new Model(1L, "Name");
		
		when(repository.findByName(anyString())).thenReturn(Optional.of(expected));
		
		Model actual = service.findByNameOrSaveNew(name);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(repository, times(1)).findByName(anyString());
	}
	
	@Test
	void findByNameOrSaveNew_ModelNotExists_CalledMethodAndReturnsExpected() {
		
		String name = "Name";
		Model expected = new Model(1L, "Name");
		
		when(repository.findByName(anyString())).thenReturn(Optional.empty());
		when(repository.save(any(Model.class))).thenReturn(expected);
		
		Model actual = service.findByNameOrSaveNew(name);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(repository, times(1)).findByName(anyString());
		verify(repository, times(1)).save(any(Model.class));
	}

	@Test
	void filterModels_ValidValue_CalledMethodAndReturnsExpected() {
		
		String name = "Name";
		Pageable pageable = PageRequest.of(0, 10);
		Model model = new Model(1L, "Name");
		Page<Model> expected = new PageImpl<Model>(List.of(model));
		
		when(repository.findAll(any(Specification.class), eq(pageable))).thenReturn(expected);
		
		Page<Model> actual = service.filterModels(name, pageable);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(repository, times(1)).findAll(any(Specification.class), eq(pageable));
	}

}
