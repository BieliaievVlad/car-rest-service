package com.foxminded.tasks.car_rest_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.foxminded.tasks.car_rest_service.entity.Make;

@Repository
public interface MakeRepository extends JpaRepository<Make, Long>, JpaSpecificationExecutor<Make> {
	
	Optional<Make> findByName(String name);
	boolean existsByName(String name);

}
