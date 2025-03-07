package com.foxminded.tasks.car_rest_service.specification;

import java.time.Year;

import org.springframework.data.jpa.domain.Specification;

import com.foxminded.tasks.car_rest_service.entity.Car;

public class CarSpecification {
	
	public static Specification<Car> filterByMake(String makeName) {
        return (root, query, criteriaBuilder) -> {
            if (makeName != null) {
                return criteriaBuilder.equal(root.get("make").get("name"), makeName);
            }
            return criteriaBuilder.conjunction();
        };
	}
	public static Specification<Car> filterByModel(String modelName) {
        return (root, query, criteriaBuilder) -> {
            if (modelName != null) {
                return criteriaBuilder.equal(root.get("model").get("name"), modelName);
            }
            return criteriaBuilder.conjunction();
        };
	}
	public static Specification<Car> filterByCategory(String categoryName) {
        return (root, query, criteriaBuilder) -> {
            if (categoryName != null) {
                return criteriaBuilder.equal(root.get("category").get("name"), categoryName);
            }
            return criteriaBuilder.conjunction();
        };
	}
	public static Specification<Car> filterByYear(Integer year) {
        return (root, query, criteriaBuilder) -> {
            if (year != null) {
                return criteriaBuilder.equal(root.get("year"), Year.of(year));
            }
            return criteriaBuilder.conjunction();
        };
	}
}
