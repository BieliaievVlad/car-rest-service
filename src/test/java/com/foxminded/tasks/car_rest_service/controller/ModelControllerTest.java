package com.foxminded.tasks.car_rest_service.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.tasks.car_rest_service.dto.model.ModelDTO;
import com.foxminded.tasks.car_rest_service.dto.model.UpsertModelDTO;
import com.foxminded.tasks.car_rest_service.entity.Model;
import com.foxminded.tasks.car_rest_service.service.CarService;
import com.foxminded.tasks.car_rest_service.service.ModelService;

import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(ModelController.class)
@WithMockUser
class ModelControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	ModelService service;
	
	@MockBean
	CarService carService;
	
	@Test
	void getFilteredModels_ValidRequest_ReturnsModels() throws Exception {
		
		ModelDTO modelDto = new ModelDTO(1L, "Name");
		Page<ModelDTO> page = new PageImpl<>(List.of(modelDto), PageRequest.of(0, 10), 1);
		
		when(service.filterModels(any(), any(Pageable.class))).thenReturn(page);
		
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/models"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Name"));
        
        verify(service, times(1)).filterModels(any(), any(Pageable.class));	
	}

	@Test
	void getModel_ModelExists_ReturnsModel() throws Exception {
		
		Long id = 1L;
		ModelDTO modelDto = new ModelDTO(1L, "Name");
		
		when(service.findById(anyLong())).thenReturn(modelDto);
		
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/models/{id}", id))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Name"));
        
        verify(service, times(1)).findById(anyLong());
	}
	
	@Test
	void getModel_ModelNotExists_ReturnsNotFound() throws Exception {
		
		Long id = 1L;
		
		when(service.findById(anyLong())).thenThrow(new EntityNotFoundException());
		
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/models/{id}", id))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
        
        verify(service, times(1)).findById(anyLong());
	}

	@Test
	void createModel_ValidModel_ReturnsCreated() throws Exception {

		ModelDTO modelDto = new ModelDTO(1L, "Name");
		String modelJson = objectMapper.writeValueAsString(modelDto);
		
		when(service.createModel(any(UpsertModelDTO.class))).thenReturn(modelDto);
		
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/models")
        		.contentType("application/json")
        		.content(modelJson)
        		.with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Name"));
        
        verify(service, times(1)).createModel(any(UpsertModelDTO.class));
	}
	
	@Test
	void createModel_InvalidModel_ReturnsBadRequest() throws Exception {
	
		when(service.createModel(any(UpsertModelDTO.class))).thenThrow(new IllegalArgumentException());
		
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/models")
        		.with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	@Test
	void updateModel_ValidModel_ReturnsOk() throws Exception {
		
		Long id = 1L;
		ModelDTO modelDto = new ModelDTO(1L, "Name");
		String modelJson = objectMapper.writeValueAsString(modelDto);
		
		when(service.updateModel(anyLong(), any(UpsertModelDTO.class))).thenReturn(modelDto);
		
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/models/{id}", id)
        		.contentType("application/json")
        		.content(modelJson)
        		.with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Name"));
        
        verify(service, times(1)).updateModel(anyLong(), any(UpsertModelDTO.class));
	}
	
	@Test
	void updateModel_InvalidModel_ReturnsBadRequest() throws Exception {
		
		Long id = 1L;
		
		when(service.updateModel(anyLong(), any(UpsertModelDTO.class))).thenThrow(new IllegalArgumentException());
		
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/models/{id}", id)
        		.with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	void deleteModel_ValidId_ReturnsNoContent() throws Exception {
		
		Long id = 1L;
		
		doNothing().when(carService).deleteModelAndAssociations(anyLong());
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/models/{id}", id)
				.with(csrf()))
		.andExpect(MockMvcResultMatchers.status().isNoContent());
		
		verify(carService, times(1)).deleteModelAndAssociations(anyLong());
	}
	
	@Test
	void deleteModel_InvalidId_ReturnsBadRequest() throws Exception {
		
		Long id = 1L;
		
		doThrow(new IllegalArgumentException()).when(carService).deleteModelAndAssociations(anyLong());
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/models/{id}", id)
				.with(csrf()))
		.andExpect(MockMvcResultMatchers.status().isBadRequest());
		
		verify(carService, times(1)).deleteModelAndAssociations(anyLong());
	}

}
