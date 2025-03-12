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
import org.springframework.test.context.ActiveProfiles;

import com.foxminded.tasks.car_rest_service.entity.Category;
import com.foxminded.tasks.car_rest_service.repository.CategoryRepository;

@SpringBootTest
class CategoryServiceTest {
	
	@Mock 
	CategoryRepository repository;
	
	@InjectMocks
	CategoryService service;

	@Test
	void findAll_ValidValue_CalledMethodAndReturnsExpected() {
		
		List<Category> expected = List.of(new Category(1L, "Category_Name"));
		
		when(repository.findAll()).thenReturn(expected);
		
		List<Category> actual = service.findAll();
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(repository, times(1)).findAll();
	}

	@Test
	void findById_ValidValue_CalledMethodAndReturnsExpected() {
		
		Long id = 1L;
		Category expected = new Category(1L, "Category_Name");
		
		when(repository.findById(anyLong())).thenReturn(Optional.of(expected));
		
		Category actual = service.findById(id);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(repository, times(1)).findById(anyLong());
	}

	@Test
	void save_ValidValue_CalledMethodAndReturnsExpected() {
		
		Category expected = new Category(1L, "Category_Name");
		
		when(repository.save(any(Category.class))).thenReturn(expected);
		
		Category actual = service.save(expected);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(repository, times(1)).save(any(Category.class));
	}

	@Test
	void delete_ValidValue_CalledMethod() {
		
		Category category = new Category(1L, "Category_Name");
		
		doNothing().when(repository).delete(any(Category.class));
		
		service.delete(category);
		
		verify(repository, times(1)).delete(any(Category.class));
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
	void findByNameOrSaveNew_CategoryExists_CalledMethodAndReturnsExpected() {
		
		String name = "Category_Name";
		Category expected = new Category(1L, "Category_Name");
		
		when(repository.findByName(anyString())).thenReturn(Optional.of(expected));
		
		Category actual = service.findByNameOrSaveNew(name);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(repository, times(1)).findByName(anyString());
	}
	
	@Test
	void findByNameOrSaveNew_CategoryNotExists_CalledMethodAndReturnsExpected() {
		
		String name = "Category_Name";
		Category expected = new Category(1L, "Category_Name");
		
		when(repository.findByName(anyString())).thenReturn(Optional.empty());
		when(repository.save(any(Category.class))).thenReturn(expected);
		
		Category actual = service.findByNameOrSaveNew(name);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(repository, times(1)).findByName(anyString());
		verify(repository, times(1)).save(any(Category.class));
	}

	@Test
	void filterCategories_ValidValue_CalledMethodAndReturnsExpected() {
		
		String name = "Category_Name";
		Pageable pageable = PageRequest.of(0, 10);
		Category category = new Category(1L, "Name");
		Page<Category> expected = new PageImpl<Category>(List.of(category));
		
		when(repository.findAll(any(Specification.class), eq(pageable))).thenReturn(expected);
		
		Page<Category> actual = service.filterCategories(name, pageable);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(repository, times(1)).findAll(any(Specification.class), eq(pageable));
	}

}
