package com.foxminded.tasks.car_rest_service.specification;

import org.springframework.data.jpa.domain.Specification;

import com.foxminded.tasks.car_rest_service.entity.Make;

public class MakeSpecification {

	public static Specification<Make> filterByName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name != null) {
                return criteriaBuilder.equal(root.get("name"), name);
            }
            return criteriaBuilder.conjunction();
        };
	}
}
