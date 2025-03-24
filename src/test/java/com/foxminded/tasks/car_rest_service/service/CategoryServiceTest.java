package com.foxminded.tasks.car_rest_service.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertThrows;
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
import com.foxminded.tasks.car_rest_service.dto.category.CategoryDTO;
import com.foxminded.tasks.car_rest_service.dto.category.UpsertCategoryDTO;
import com.foxminded.tasks.car_rest_service.entity.Category;
import com.foxminded.tasks.car_rest_service.mapper.CategoryMapper;
import com.foxminded.tasks.car_rest_service.repository.CategoryRepository;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
	
	@Mock 
	CategoryRepository repository;
	
	@Mock
	CategoryMapper mapper;
	
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
	void findCategoryById_ValidId_CalledMethodsAndReturnsExpected() {
		
		long id = 1L;
		Category category = new Category(1L, "Name");
		CategoryDTO expected = new CategoryDTO(1L, "Name");
		
		when(repository.findById(anyLong())).thenReturn(Optional.of(category));
		when(mapper.categoryToDto(any(Category.class))).thenReturn(expected);
		
		CategoryDTO actual = service.findCategoryById(id);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(repository, times(1)).findById(anyLong());
		verify(mapper, times(1)).categoryToDto(any(Category.class));
	}
	
	@Test
	void createCategory_ValidValue_CalledMethodAndReturnsExpected() {
		
		Category category = new Category(1L, "Name");
		UpsertCategoryDTO createDto = new UpsertCategoryDTO("Name");
		CategoryDTO expected = new CategoryDTO(1L, "Name");
		
		when(repository.existsByName(anyString())).thenReturn(false);
		when(repository.save(any(Category.class))).thenReturn(category);
		when(mapper.categoryToDto(any(Category.class))).thenReturn(expected);
		
		CategoryDTO actual = service.createCategory(createDto);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(repository, times(1)).existsByName(anyString());
		verify(repository, times(1)).save(any(Category.class));
		verify(mapper, times(1)).categoryToDto(any(Category.class));
	}
	
	@Test
	void createCategory_InvalidValue_CalledMethodAndReturnsExpected() {

		UpsertCategoryDTO categoryDto = new UpsertCategoryDTO("Name");
		
		when(repository.existsByName(anyString())).thenReturn(true);
		
		assertThrows(IllegalArgumentException.class, () -> {
			service.createCategory(categoryDto);
		});
		verify(repository, times(1)).existsByName(anyString());
	}
	
	@Test
	void updateCategory_ValidCategory_CalledMethodsAndReturnsExpected() {
		
		Long id = 1L;
		Category category = new Category(1L, "Name");
		UpsertCategoryDTO updateDto = new UpsertCategoryDTO("Name");
		CategoryDTO expected = new CategoryDTO(1L, "Category_Name");
		
		when(repository.findById(anyLong())).thenReturn(Optional.of(category));
		when(repository.save(any(Category.class))).thenReturn(category);
		when(mapper.categoryToDto(any(Category.class))).thenReturn(expected);
		
		CategoryDTO actual = service.updateCategory(id, updateDto);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(repository, times(1)).findById(anyLong());
		verify(repository, times(1)).save(any(Category.class));
		verify(mapper, times(1)).categoryToDto(any(Category.class));
	}

	@Test
	void filterCategories_ValidValue_CalledMethodAndReturnsExpected() {
		
		String name = "Name";
		Pageable pageable = PageRequest.of(0, 10);
		Category category = new Category(1L, "Name");
		CategoryDTO categoryDto = new CategoryDTO(1L, "Name");
		Page<Category> page = new PageImpl<Category>(List.of(category));
		Page<CategoryDTO> expected = new PageImpl<CategoryDTO>(List.of(categoryDto));
		
		when(repository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
		when(mapper.categoryToDto(any(Category.class))).thenReturn(categoryDto);
		
		Page<CategoryDTO> actual = service.filterCategories(name, pageable);
		
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		verify(repository, times(1)).findAll(any(Specification.class), eq(pageable));
		verify(mapper, times(1)).categoryToDto(any(Category.class));
	}

}
