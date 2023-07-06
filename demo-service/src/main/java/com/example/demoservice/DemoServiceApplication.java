package com.example.demoservice;

import com.example.demoservice.model.CarModel;
import com.example.demoservice.repository.CarModelRepository;
import com.maphb.MapHBContext;
import com.maphb.models.TableFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class DemoServiceApplication {

	public static void main(String[] args) {
		MapHBContext.startApplication(DemoServiceApplication.class);

		CarModel carModel = new CarModel("GOL", "2025");
		CarModel carModel1 = new CarModel();

		carModel1.setYearModel("2002");

		CarModelRepository carModelRepository = new CarModelRepository(CarModel.class);
		carModelRepository.put(carModel);


		TableFilter<CarModel> t = new TableFilter<>(CarModel.class);
		t.setFilterByModelExample(carModel1);
		List<CarModel> carModelList = carModelRepository.scan(t);

		for (CarModel carModel2 : carModelList) {
			System.out.println(carModel2);
		}

		carModelRepository.delete("GOL#2002");

		System.out.println(Objects.isNull(carModelRepository.get("GOL#2002")));


	}

//	@Bean
//	public Connection connection() {
//		return MapHBContext.getConnection();
//	}

}


