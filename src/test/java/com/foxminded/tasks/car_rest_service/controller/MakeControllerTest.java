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
import com.foxminded.tasks.car_rest_service.dto.make.UpsertMakeDTO;
import com.foxminded.tasks.car_rest_service.dto.make.MakeDTO;
import com.foxminded.tasks.car_rest_service.service.CarService;
import com.foxminded.tasks.car_rest_service.service.MakeService;

import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(MakeController.class)
@WithMockUser
class MakeControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	MakeService service;
	
	@MockBean
	CarService carService;
	
	@Test
	void getFilteredMakes_ValidRequest_ReturnsMakes() throws Exception {
		
		MakeDTO makeDto = new MakeDTO(1L, "Name");
		Page<MakeDTO> page = new PageImpl<>(List.of(makeDto), PageRequest.of(0, 10), 1);
		
		when(service.filterMakes(any(), any(Pageable.class))).thenReturn(page);
		
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/makes"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Name"));
        
        verify(service, times(1)).filterMakes(any(), any(Pageable.class));	
	}

	@Test
	void getMake_MakeExists_ReturnsMake() throws Exception {
		
		Long id = 1L;
		MakeDTO makeDto = new MakeDTO(1L, "Name");
		
		when(service.findById(anyLong())).thenReturn(makeDto);
		
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/makes/{id}", id))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Name"));
        
        verify(service, times(1)).findById(anyLong());
	}
	
	@Test
	void getMake_MakeNotExists_ReturnsNotFound() throws Exception {
		
		Long id = 1L;
		
		when(service.findById(anyLong())).thenThrow(new EntityNotFoundException());
		
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/makes/{id}", id))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
        
        verify(service, times(1)).findById(anyLong());
	}

	@Test
	void createMake_ValidMake_ReturnsCreated() throws Exception {

		MakeDTO makeDto = new MakeDTO(1L, "Name");
		String makeJson = objectMapper.writeValueAsString(makeDto);
		
		when(service.createMake(any(UpsertMakeDTO.class))).thenReturn(makeDto);
		
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/makes")
        		.contentType("application/json")
        		.content(makeJson)
        		.with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Name"));
        
        verify(service, times(1)).createMake(any(UpsertMakeDTO.class));
	}
	
	@Test
	void createMake_InvalidMake_ReturnsBadRequest() throws Exception {
	
		when(service.createMake(any(UpsertMakeDTO.class))).thenThrow(new IllegalArgumentException());
		
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/makes")
        		.with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	@Test
	void updateMake_ValidMake_ReturnsOk() throws Exception {
		
		Long id = 1L;
		MakeDTO makeDto = new MakeDTO(1L, "Name");
		String makeJson = objectMapper.writeValueAsString(makeDto);
		
		when(service.updateMake(anyLong(), any(UpsertMakeDTO.class))).thenReturn(makeDto);
		
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/makes/{id}", id)
        		.contentType("application/json")
        		.content(makeJson)
        		.with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Name"));
        
        verify(service, times(1)).updateMake(anyLong(), any(UpsertMakeDTO.class));
	}
	
	@Test
	void updateMake_InvalidMake_ReturnsBadRequest() throws Exception {
		
		Long id = 1L;
		
		when(service.updateMake(anyLong(), any(UpsertMakeDTO.class))).thenThrow(new IllegalArgumentException());
		
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/makes/{id}", id)
        		.with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	void deleteMake_ValidId_ReturnsNoContent() throws Exception {
		
		Long id = 1L;
		
		doNothing().when(carService).deleteMakeAndAssociations(anyLong());
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/makes/{id}", id)
				.with(csrf()))
		.andExpect(MockMvcResultMatchers.status().isNoContent());
		
		verify(carService, times(1)).deleteMakeAndAssociations(anyLong());
	}
	
	@Test
	void deleteMake_InvalidId_ReturnsBadRequest() throws Exception {
		
		Long id = 1L;
		
		doThrow(new IllegalArgumentException()).when(carService).deleteMakeAndAssociations(anyLong());
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/makes/{id}", id)
				.with(csrf()))
		.andExpect(MockMvcResultMatchers.status().isBadRequest());
		
		verify(carService, times(1)).deleteMakeAndAssociations(anyLong());
	}

}
