package com.example.demoservice;

import com.example.demoservice.model.CarModel;
import com.example.demoservice.repository.CarModelRepository;
import com.maphb.MapHBContext;
import com.maphb.manager.EntityRepositoryFactory;
import org.apache.hadoop.hbase.client.Connection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@SpringBootApplication
@ConfigurationPropertiesScan
public class DemoServiceApplication {

	public static void main(String[] args) throws IOException {
		MapHBContext.startApplication(DemoServiceApplication.class);

		CarModelRepository carModelRepository = new CarModelRepository(CarModel.class);
		System.out.println(carModelRepository.get("0001"));

		SpringApplication.run(DemoServiceApplication.class, args);
	}

//	@Bean
//	public Connection connection() {
//		return MapHBContext.getConnection();
//	}

}


