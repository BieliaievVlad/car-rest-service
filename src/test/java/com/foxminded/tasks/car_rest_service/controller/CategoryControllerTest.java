package com.foxminded.tasks.car_rest_service.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.tasks.car_rest_service.dto.category.CategoryDTO;
import com.foxminded.tasks.car_rest_service.dto.category.UpsertCategoryDTO;
import com.foxminded.tasks.car_rest_service.service.CarService;
import com.foxminded.tasks.car_rest_service.service.CategoryService;

import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	CategoryService service;
	
	@MockBean
	CarService carService;
	
	@Test
	void getFilteredCategories_ValidRequest_ReturnsCategories() throws Exception {
		
		CategoryDTO categoryDto = new CategoryDTO(1L, "Name");
		Page<CategoryDTO> page = new PageImpl<>(List.of(categoryDto), PageRequest.of(0, 10), 1);
		
		when(service.filterCategories(any(), any(Pageable.class))).thenReturn(page);
		
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/categories"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Name"));
        
        verify(service, times(1)).filterCategories(any(), any(Pageable.class));	
	}

	@Test
	void getCategory_CategoryExists_ReturnsCategory() throws Exception {
		
		Long id = 1L;
		CategoryDTO categoryDto = new CategoryDTO(1L, "Name");
		
		when(service.findCategoryById(anyLong())).thenReturn(categoryDto);
		
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/categories/{id}", id))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Name"));
        
        verify(service, times(1)).findCategoryById(anyLong());
	}
	
	@Test
	void getCategory_CategoryNotExists_ReturnsNotFound() throws Exception {
		
		Long id = 1L;
		
		when(service.findCategoryById(anyLong())).thenThrow(new EntityNotFoundException());
		
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/categories/{id}", id))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
        
        verify(service, times(1)).findCategoryById(anyLong());
	}

	@Test
	void createCategory_ValidCategory_ReturnsCreated() throws Exception {

		CategoryDTO categoryDto = new CategoryDTO(1L, "Name");
		String categoryJson = objectMapper.writeValueAsString(categoryDto);
		
		when(service.createCategory(any(UpsertCategoryDTO.class))).thenReturn(categoryDto);
		
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/categories")
        		.contentType("application/json")
        		.content(categoryJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Name"));
        
        verify(service, times(1)).createCategory(any(UpsertCategoryDTO.class));
	}
	
	@Test
	void createCategory_InvalidCategory_ReturnsBadRequest() throws Exception {
	
		when(service.createCategory(any(UpsertCategoryDTO.class))).thenThrow(new IllegalArgumentException());
		
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/categories"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	@Test
	void updateCategory_ValidCategory_ReturnsOk() throws Exception {
		
		Long id = 1L;
		CategoryDTO categoryDto = new CategoryDTO(1L, "Name");
		String categoryJson = objectMapper.writeValueAsString(categoryDto);
		
		when(service.updateCategory(anyLong(), any(UpsertCategoryDTO.class))).thenReturn(categoryDto);
		
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/categories/{id}", id)
        		.contentType("application/json")
        		.content(categoryJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Name"));
        
        verify(service, times(1)).updateCategory(anyLong(), any(UpsertCategoryDTO.class));
	}
	
	@Test
	void updateCategory_InvalidCategory_ReturnsBadRequest() throws Exception {
		
		Long id = 1L;
		
		when(service.updateCategory(anyLong(), any(UpsertCategoryDTO.class))).thenThrow(new IllegalArgumentException());
		
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/categories/{id}", id))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	void deleteCategory_ValidId_ReturnsNoContent() throws Exception {
		
		Long id = 1L;
		
		doNothing().when(carService).deleteCategoryAndAssociations(anyLong());
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/categories/{id}", id))
		.andExpect(MockMvcResultMatchers.status().isNoContent());
		
		verify(carService, times(1)).deleteCategoryAndAssociations(anyLong());
	}
	
	@Test
	void deleteCategory_InvalidId_ReturnsBadRequest() throws Exception {
		
		Long id = 1L;
		
		doThrow(new IllegalArgumentException()).when(carService).deleteCategoryAndAssociations(anyLong());
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/categories/{id}", id))
		.andExpect(MockMvcResultMatchers.status().isBadRequest());
		
		verify(carService, times(1)).deleteCategoryAndAssociations(anyLong());
	}

}
