package com.foxminded.tasks.car_rest_service.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Year;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxminded.tasks.car_rest_service.entity.Car;
import com.foxminded.tasks.car_rest_service.entity.Category;
import com.foxminded.tasks.car_rest_service.entity.Make;
import com.foxminded.tasks.car_rest_service.entity.Model;
import com.foxminded.tasks.car_rest_service.repository.CarRepository;
import com.foxminded.tasks.car_rest_service.repository.CategoryRepository;
import com.foxminded.tasks.car_rest_service.repository.MakeRepository;
import com.foxminded.tasks.car_rest_service.repository.ModelRepository;
import jakarta.annotation.PostConstruct;

@Service
public class DataImportService {

	private CarRepository carRepository;
	private MakeRepository makeRepository;
	private ModelRepository modelRepository;
	private CategoryRepository categoryRepository;
	private final String filePath = "file.csv";
	Logger logger = LoggerFactory.getLogger(DataImportService.class);

	@Autowired
	public DataImportService(CarRepository carRepository, MakeRepository makeRepository,
			ModelRepository modelRepository, CategoryRepository categoryRepository) {
		this.carRepository = carRepository;
		this.makeRepository = makeRepository;
		this.modelRepository = modelRepository;
		this.categoryRepository = categoryRepository;
	}

	@PostConstruct
	public void initData() {

		if (carRepository.count() == 0 && makeRepository.count() == 0 && modelRepository.count() == 0
				&& categoryRepository.count() == 0) {

			try {

				importDataFromCsv(filePath);
				logger.info("Data successfully imported.");

			} catch (Exception e) {
				logger.error("Error importing data from CSV file.");
				e.printStackTrace();
			}

		} else {
			logger.info("Data import scipped. Database is not empty.");
		}
	}

	private void importDataFromCsv(String filePath) throws Exception {

		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);

		if (inputStream == null) {
			throw new FileNotFoundException("CSV file not found in resources.");
		}

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);

			for (CSVRecord rec : records) {

				if (isEmptyRecord(rec)) {
					logger.warn("Skipping empty or malformed record: {} ", rec);
					continue;
				}

				String objectId = rec.get("objectId");
				String makeName = rec.get("Make");
				Year year = Year.of(Integer.parseInt(rec.get("Year")));
				String modelName = rec.get("Model");
				String categoryName = rec.get("Category");

				Make make = makeRepository.findByName(makeName)
						.orElseGet(() -> makeRepository.save(new Make(makeName)));

				Model model = modelRepository.findByName(modelName)
						.orElseGet(() -> modelRepository.save(new Model(modelName)));

				Category category = categoryRepository.findByName(categoryName)
						.orElseGet(() -> categoryRepository.save(new Category(categoryName)));

				Car car = new Car(make, model, category, year, objectId);

				carRepository.save(car);
			}

		} catch (Exception e) {
			logger.error("Error processing CSV file", e);
			throw e;
		}
	}

	private boolean isEmptyRecord(CSVRecord rec) {

		return rec.size() == 0 || rec.stream().allMatch(String::isEmpty);
	}
}
