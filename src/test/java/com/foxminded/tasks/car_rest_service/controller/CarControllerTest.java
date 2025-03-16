package com.foxminded.tasks.car_rest_service.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Year;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.foxminded.tasks.car_rest_service.dto.CarDTO;
import com.foxminded.tasks.car_rest_service.entity.Car;
import com.foxminded.tasks.car_rest_service.entity.Category;
import com.foxminded.tasks.car_rest_service.entity.Make;
import com.foxminded.tasks.car_rest_service.entity.Model;
import com.foxminded.tasks.car_rest_service.service.CarService;
import com.foxminded.tasks.car_rest_service.service.DataManagementService;

import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(CarController.class)
class CarControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	CarService carService;
	
	@MockBean
	DataManagementService service;
	
	@Test
	void getFilteredCars_ValidRequest_ReturnsCars() throws Exception {
		
		CarDTO carDto = new CarDTO(1L, "Make_Name", "Model_Name", "Category_Name", 2025, "ObjectId");
		
        Page<CarDTO> carDtoPage = new PageImpl<>(List.of(carDto), PageRequest.of(0, 10), 1);

        when(carService.filterCars(any(), any(), any(), any(), any(Pageable.class))).thenReturn(carDtoPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cars"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].make").value("Make_Name"));
        
        verify(carService, times(1)).filterCars(any(), any(), any(), any(), any(Pageable.class));
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
		
		Long id = 1L;
		Make make = new Make(1L, "Make_Name");
		Model model = new Model(1L, "Model_Name");
		Category category = new Category(1L, "Category_Name");
		Year year = Year.of(2025);
		String objectId = "ObjectId";
		Car car = new Car(id, make, model, category, year, objectId);
		CarDTO carDto = new CarDTO(1L, "Make_Name", "Model_Name", "Category_Name", 2025, "ObjectId");
		String carDtoJson = objectMapper.writeValueAsString(carDto);
		
		when(service.createCar(any(CarDTO.class))).thenReturn(car);
		
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cars")
        		.contentType("application/json")
        		.content(carDtoJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.make").value("Make_Name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.model").value("Model_Name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("Category_Name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.year").value(2025))
        		.andExpect(MockMvcResultMatchers.jsonPath("$.objectId").value("ObjectId"));
        
        verify(service, times(1)).createCar(any(CarDTO.class));
	}
	
	@Test
	void createCar_InvalidCar_ReturnsBadRequest() throws Exception {
		
		when(service.createCar(any(CarDTO.class))).thenThrow(new IllegalArgumentException());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cars"))
		.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	void deleteCar_ValidId_ReturnsNoContent() throws Exception {
		
		Long id = 1L;
		
		doNothing().when(service).deleteCarById(anyLong());
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/cars/{id}", id))
		.andExpect(MockMvcResultMatchers.status().isNoContent());
		
		verify(service, times(1)).deleteCarById(anyLong());
	}
	
	@Test
	void deleteCar_InvalidId_ReturnsBadRequest() throws Exception {
		
		Long id = 1L;
		
		doThrow(new IllegalArgumentException()).when(service).deleteCarById(anyLong());
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/cars/{id}", id))
		.andExpect(MockMvcResultMatchers.status().isBadRequest());
		
		verify(service, times(1)).deleteCarById(anyLong());
	}

}
