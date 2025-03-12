package com.foxminded.tasks.car_rest_service.entity;

import java.time.Year;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "car", schema = "car_service")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Car {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "make_id", nullable = false)
	@JsonProperty("make")
	private Make make;
	
	@ManyToOne
	@JoinColumn(name = "model_id", nullable = false)
	@JsonProperty("model")
	private Model model;
	
	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false)
	@JsonProperty("category")
	private Category category;
	
	@Column(name = "year", nullable = false)
	@JsonProperty("year")
	private Year year;
	
	@Column(name = "objectId", nullable = false)
	@JsonProperty("objectId")
	private String objectId;
	
	public Car(Make make, Model model, Category category, Year year, String objectId) {
		this.make = make;
		this.model = model;
		this.category = category;
		this.year = year;
		this.objectId = objectId;
	}
}
