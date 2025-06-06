package com.foxminded.tasks.car_rest_service.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.*;
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
import com.foxminded.tasks.car_rest_service.dto.model.ModelDTO;
import com.foxminded.tasks.car_rest_service.dto.model.UpsertModelDTO;
import com.foxminded.tasks.car_rest_service.entity.Make;
import com.foxminded.tasks.car_rest_service.entity.Model;
import com.foxminded.tasks.car_rest_service.mapper.ModelMapper;
import com.foxminded.tasks.car_rest_service.repository.ModelRepository;

@ExtendWith(MockitoExtension.class)
class ModelServiceTest {
	
	@Mock
	ModelRepository repository;
	
	@Mock
	ModelMapper mapper;
	
	@InjectMocks
	ModelService service;

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
		Model model = new Model(1L, "Name");
		ModelDTO expected = new ModelDTO(1L, "Name");
		
		when(repository.findByName(anyString())).thenReturn(Optional.of(model));
		when(mapper.modelToDto(any(Model.class))).thenReturn(expected);
		
		ModelDTO actual = service.findByNameOrSaveNew(name);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(repository, times(1)).findByName(anyString());
		verify(mapper, times(1)).modelToDto(any(Model.class));
	}
	
	@Test
	void findByNameOrSaveNew_ModelNotExists_CalledMethodAndReturnsExpected() {
		
		String name = "Name";
		Model model = new Model(1L, "Name");
		ModelDTO expected = new ModelDTO(1L, "Name");
		
		when(repository.findByName(anyString())).thenReturn(Optional.empty());
		when(repository.save(any(Model.class))).thenReturn(model);
		when(mapper.modelToDto(any(Model.class))).thenReturn(expected);
		
		ModelDTO actual = service.findByNameOrSaveNew(name);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(repository, times(1)).findByName(anyString());
		verify(repository, times(1)).save(any(Model.class));
		verify(mapper, times(1)).modelToDto(any(Model.class));
	}
	
	@Test
	void findById_ValidId_CalledMethodsAndReturnsExpected() {
		
		long id = 1L;
		Model model = new Model(1L, "Name");
		ModelDTO expected = new ModelDTO(1L, "Name");
		
		when(repository.findById(anyLong())).thenReturn(Optional.of(model));
		when(mapper.modelToDto(any(Model.class))).thenReturn(expected);
		
		ModelDTO actual = service.findById(id);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(repository, times(1)).findById(anyLong());
		verify(mapper, times(1)).modelToDto(any(Model.class));
	}
	
	@Test
	void createModel_ValidValue_CalledMethodAndReturnsExpected() {
		
		Model model = new Model(1L, "Name");
		UpsertModelDTO createDto = new UpsertModelDTO("Name");
		ModelDTO expected = new ModelDTO(1L, "Name");
		
		when(repository.existsByName(anyString())).thenReturn(false);
		when(repository.save(any(Model.class))).thenReturn(model);
		when(mapper.modelToDto(any(Model.class))).thenReturn(expected);
		
		ModelDTO actual = service.createModel(createDto);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(repository, times(1)).existsByName(anyString());
		verify(repository, times(1)).save(any(Model.class));
		verify(mapper, times(1)).modelToDto(any(Model.class));
	}
	
	@Test
	void createModel_InvalidValue_CalledMethodAndReturnsExpected() {

		UpsertModelDTO modelDto = new UpsertModelDTO("Name");
		
		when(repository.existsByName(anyString())).thenReturn(true);
		
		assertThrows(IllegalArgumentException.class, () -> {
			service.createModel(modelDto);
		});
		verify(repository, times(1)).existsByName(anyString());
	}
	
	@Test
	void updateModel_ValidMake_CalledMethodsAndReturnsExpected() {
		
		Long id = 1L;
		Model model = new Model(1L, "Name");
		UpsertModelDTO updateDto = new UpsertModelDTO("Name");
		ModelDTO expected = new ModelDTO(1L, "Make_Name");
		
		when(repository.findById(anyLong())).thenReturn(Optional.of(model));
		when(repository.save(any(Model.class))).thenReturn(model);
		when(mapper.modelToDto(any(Model.class))).thenReturn(expected);
		
		ModelDTO actual = service.updateModel(id, updateDto);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(repository, times(1)).findById(anyLong());
		verify(repository, times(1)).save(any(Model.class));
		verify(mapper, times(1)).modelToDto(any(Model.class));
	}
	
	@Test
	void delete_ValidId_CalledMethods() {
		
		Long id = 1L;
		ModelDTO modelDto = new ModelDTO(1L, "Name");
		Model model = new Model(1L, "Name");
		
		when(repository.findById(anyLong())).thenReturn(Optional.of(model));
		when(mapper.modelToDto(any(Model.class))).thenReturn(modelDto);
		when(mapper.dtoToModel(any(ModelDTO.class))).thenReturn(model);
		doNothing().when(repository).delete(any(Model.class));
		
		service.delete(id);
		
		verify(repository, times(1)).findById(anyLong());
		verify(mapper, times(1)).modelToDto(any(Model.class));
		verify(mapper, times(1)).dtoToModel(any(ModelDTO.class));
		verify(repository, times(1)).delete(any(Model.class));
	}

	@Test
	void filterModels_ValidValue_CalledMethodAndReturnsExpected() {
		
		String name = "Name";
		Pageable pageable = PageRequest.of(0, 10);
		Model model = new Model(1L, "Name");
		ModelDTO modelDto = new ModelDTO(1L, "Name");
		Page<Model> page = new PageImpl<Model>(List.of(model));
		Page<ModelDTO> expected = new PageImpl<ModelDTO>(List.of(modelDto));
		
		when(repository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
		when(mapper.modelToDto(any(Model.class))).thenReturn(modelDto);
		
		Page<ModelDTO> actual = service.filterModels(name, pageable);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(repository, times(1)).findAll(any(Specification.class), eq(pageable));
		verify(mapper, times(1)).modelToDto(any(Model.class));
	}

}
