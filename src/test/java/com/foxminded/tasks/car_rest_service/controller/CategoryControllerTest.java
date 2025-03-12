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
import com.foxminded.tasks.car_rest_service.entity.Category;
import com.foxminded.tasks.car_rest_service.service.CategoryService;
import com.foxminded.tasks.car_rest_service.service.DataManagementService;

import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	CategoryService categoryService;
	
	@MockBean
	DataManagementService service;
	
	@Test
	void getFilteredCategories_ValidRequest_ReturnsCategories() throws Exception {
		
		Category category = new Category(1L, "Name");
		Page<Category> page = new PageImpl<>(List.of(category), PageRequest.of(0, 10), 1);
		
		when(categoryService.filterCategories(any(), any(Pageable.class))).thenReturn(page);
		
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/categories"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Name"));
        
        verify(categoryService, times(1)).filterCategories(any(), any(Pageable.class));	
	}

	@Test
	void getCategory_CategoryExists_ReturnsCategory() throws Exception {
		
		Long id = 1L;
		Category category = new Category(1L, "Name");
		
		when(categoryService.findById(anyLong())).thenReturn(category);
		
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/categories/{id}", id))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Name"));
        
        verify(categoryService, times(1)).findById(anyLong());
	}
	
	@Test
	void getCategory_CategoryNotExists_ReturnsNotFound() throws Exception {
		
		Long id = 1L;
		
		when(categoryService.findById(anyLong())).thenThrow(new EntityNotFoundException());
		
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/categories/{id}", id))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
        
        verify(categoryService, times(1)).findById(anyLong());
	}

	@Test
	void createCategory_ValidCategory_ReturnsCreated() throws Exception {

		Category category = new Category(1L, "Name");
		String categoryJson = objectMapper.writeValueAsString(category);
		
		when(service.createCategory(any(Category.class))).thenReturn(category);
		
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/categories")
        		.contentType("application/json")
        		.content(categoryJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Name"));
        
        verify(service, times(1)).createCategory(any(Category.class));
	}
	
	@Test
	void createCategory_InvalidCategory_ReturnsBadRequest() throws Exception {
	
		when(service.createCategory(any(Category.class))).thenThrow(new IllegalArgumentException());
		
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/categories"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	void deleteCategory_ValidId_ReturnsNoContent() throws Exception {
		
		Long id = 1L;
		
		doNothing().when(service).deleteCategoryAndAssociations(anyLong());
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/categories/{id}", id))
		.andExpect(MockMvcResultMatchers.status().isNoContent());
		
		verify(service, times(1)).deleteCategoryAndAssociations(anyLong());
	}
	
	@Test
	void deleteCategory_InvalidId_ReturnsBadRequest() throws Exception {
		
		Long id = 1L;
		
		doThrow(new IllegalArgumentException()).when(service).deleteCategoryAndAssociations(anyLong());
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/categories/{id}", id))
		.andExpect(MockMvcResultMatchers.status().isBadRequest());
		
		verify(service, times(1)).deleteCategoryAndAssociations(anyLong());
	}

}
