package com.foxminded.tasks.car_rest_service.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.foxminded.tasks.car_rest_service.dto.car.CarDTO;
import com.foxminded.tasks.car_rest_service.dto.car.CarListItemDTO;
import com.foxminded.tasks.car_rest_service.dto.car.CreateCarDTO;
import com.foxminded.tasks.car_rest_service.dto.car.UpdateCarDTO;
import com.foxminded.tasks.car_rest_service.service.CarService;

import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(CarController.class)
@WithMockUser
class CarControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	CarService service;
	
	@Test
	void getFilteredCars_ValidRequest_ReturnsCars() throws Exception {
		
		CarListItemDTO carDto = new CarListItemDTO("Make_Name", "Model_Name", "Category_Name", 2025);
		
        Page<CarListItemDTO> carDtoPage = new PageImpl<>(List.of(carDto), PageRequest.of(0, 10), 1);

        when(service.filterCars(any(), any(), any(), any(), any(Pageable.class))).thenReturn(carDtoPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cars"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].make").value("Make_Name"));
        
        verify(service, times(1)).filterCars(any(), any(), any(), any(), any(Pageable.class));
	}

	@Test
	void getCar_CarExists_ReturnsCar() throws Exception {

		Long id = 1L;
		CarDTO carDto = new CarDTO(1L, "Make_Name", "Model_Name", "Category_Name", 2025, "ObjectId");
		
		when(service.findCarById(anyLong())).thenReturn(carDto);
		
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cars/{id}", id))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
        .andExpect(MockMvcResultMatchers.jsonPath("$.make").value("Make_Name"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.model").value("Model_Name"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("Category_Name"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.year").value(2025))
        .andExpect(MockMvcResultMatchers.jsonPath("$.objectId").value("ObjectId"));
        
        verify(service, times(1)).findCarById(anyLong());
	}
	
	@Test
	void getCar_CarNotExists_ReturnsNotFound() throws Exception {
		
		Long id = 1L;
		
		when(service.findCarById(anyLong())).thenThrow(new EntityNotFoundException());
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cars/{id}", id))
		.andExpect(MockMvcResultMatchers.status().isNotFound());
		
		verify(service, times(1)).findCarById(anyLong());
	}

	@Test
	void createCar_ValidCar_ReturnsCreated() throws Exception {

		CarDTO carDto = new CarDTO(1L, "Make_Name", "Model_Name", "Category_Name", 2025, "ObjectId");
		String carDtoJson = objectMapper.writeValueAsString(carDto);
		
		when(service.createCar(any(CreateCarDTO.class))).thenReturn(carDto);
		
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cars")
        		.contentType("application/json")
        		.content(carDtoJson)
        		.with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.make").value("Make_Name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.model").value("Model_Name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("Category_Name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.year").value(2025))
        		.andExpect(MockMvcResultMatchers.jsonPath("$.objectId").value("ObjectId"));
        
        verify(service, times(1)).createCar(any(CreateCarDTO.class));
	}
	
	@Test
	void createCar_InvalidCar_ReturnsBadRequest() throws Exception {
		
		String createCarDtoJson = "{\"make\": \"LADA\",\"model\": \"KALINA\",\"category\": \"Sedan\",\"year\": \"2026\",\"objectId\": \"\"}";
		
		when(service.createCar(any(CreateCarDTO.class))).thenThrow(new IllegalArgumentException());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cars")
				.contentType(MediaType.APPLICATION_JSON)
				.content(createCarDtoJson)
				.with(csrf()))
		.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	@Test
	void updateCar_ValidCar_ReturnsOk() throws Exception {
		
		Long id = 1L;
		CarDTO carDto = new CarDTO(1L, "Make_Name", "Model_Name", "Category_Name", 2025, "ObjectId");
		String carDtoJson = objectMapper.writeValueAsString(carDto);
		
		when(service.updateCar(anyLong(), any(UpdateCarDTO.class))).thenReturn(carDto);
		
	       mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/cars/{id}", id)
	        		.contentType("application/json")
	        		.content(carDtoJson)
	        		.with(csrf()))
	                .andExpect(MockMvcResultMatchers.status().isOk())
	                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
	                .andExpect(MockMvcResultMatchers.jsonPath("$.make").value("Make_Name"))
	                .andExpect(MockMvcResultMatchers.jsonPath("$.model").value("Model_Name"))
	                .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("Category_Name"))
	                .andExpect(MockMvcResultMatchers.jsonPath("$.year").value(2025))
	        		.andExpect(MockMvcResultMatchers.jsonPath("$.objectId").value("ObjectId"));
	        
	        verify(service, times(1)).updateCar(anyLong(), any(UpdateCarDTO.class));
	}
	
	@Test
	void updateCar_InvalidCarData_ReturnsBadRequest() throws Exception {
		
		Long id = 1L;
		String updateCarDtoJson = "{\"make\": \"LADA\",\"model\": \"KALINA\",\"category\": \"Sedan\",\"year\": \"2026\",\"objectId\": \"\"}";
		
		when(service.updateCar(anyLong(), any(UpdateCarDTO.class))).thenThrow(new IllegalArgumentException());
		
		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/cars/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(updateCarDtoJson)
				.with(csrf()))
		.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	@Test
	void updateCar_InvalidCarId_ReturnsNotFound() throws Exception {
		
		Long id = 1L;
		String updateCarDtoJson = "{\"make\": \"LADA\",\"model\": \"KALINA\",\"category\": \"Sedan\",\"year\": \"2026\",\"objectId\": \"\"}";
		
		when(service.updateCar(anyLong(), any(UpdateCarDTO.class))).thenThrow(new EntityNotFoundException());
		
		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/cars/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(updateCarDtoJson)
				.with(csrf()))
		.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	void delete_ValidId_ReturnsNoContent() throws Exception {
		
		Long id = 1L;
		
		doNothing().when(service).delete(anyLong());
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/cars/{id}", id)
				.with(csrf()))
		.andExpect(MockMvcResultMatchers.status().isNoContent());
		
		verify(service, times(1)).delete(anyLong());
	}
	
	@Test
	void delete_InvalidId_ReturnsNotFound() throws Exception {
		
		Long id = 1L;
		
		doThrow(new EntityNotFoundException()).when(service).delete(anyLong());
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/cars/{id}", id)
				.with(csrf()))
		.andExpect(MockMvcResultMatchers.status().isNotFound());
		
		verify(service, times(1)).delete(anyLong());
	}

}
