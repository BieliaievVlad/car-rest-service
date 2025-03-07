package com.foxminded.tasks.car_rest_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.foxminded.tasks.car_rest_service.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	Optional<Category> findByName(String name);

}
