package com.foxminded.tasks.car_rest_service.service;

import java.io.FileReader;
import java.time.Year;

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
import com.opencsv.CSVReader;

import jakarta.annotation.PostConstruct;

@Service
public class DataImportService {

	private CarRepository carRepository;
	private MakeRepository makeRepository;
	private ModelRepository modelRepository;
	private CategoryRepository categoryRepository;
	private final String filePath = getClass().getClassLoader().getResource("file.csv").getFile();
	Logger logger = LoggerFactory.getLogger(DataImportService.class);
	
	@Autowired
	public DataImportService(CarRepository carRepository, 
							 MakeRepository makeRepository,
							 ModelRepository modelRepository, 
							 CategoryRepository categoryRepository) {
		this.carRepository = carRepository;
		this.makeRepository = makeRepository;
		this.modelRepository = modelRepository;
		this.categoryRepository = categoryRepository;
	}

	@PostConstruct
	public void initData() {

		if (carRepository.count() == 0 && 
			makeRepository.count() == 0 && 
			modelRepository.count() == 0 && 
			categoryRepository.count() == 0) {

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

		try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
			String[] nextLine;

			reader.readNext();

			while ((nextLine = reader.readNext()) != null &&  
					!nextLine[0].equals("")) {

				String objectId = nextLine[0];
				String makeName = nextLine[1];
				Year year = Year.parse(nextLine[2]);
				String modelName = nextLine[3];
				String categoryName = nextLine[4];

				Make make = makeRepository.findByName(makeName)
						.orElseGet(() -> makeRepository.save(new Make(makeName)));
				
				Model model = modelRepository.findByName(modelName)
						.orElseGet(() -> modelRepository.save(new Model(modelName)));
				
				Category category = categoryRepository.findByName(categoryName)
						.orElseGet(() -> categoryRepository.save(new Category(categoryName)));
				
				Car car = new Car(make, model, category, year, objectId);

				carRepository.save(car);
			}
		}
	}
}
