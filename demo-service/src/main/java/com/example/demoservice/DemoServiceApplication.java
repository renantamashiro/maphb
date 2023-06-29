package com.example.demoservice;

import com.example.demoservice.model.CarModel;
import com.example.demoservice.repository.CarModelRepository;
import com.maphb.MapHBContext;
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
		CarModel carModel = new CarModel("GOL", "2025");
		MapHBContext.startApplication(DemoServiceApplication.class);

		CarModelRepository carModelRepository = new CarModelRepository(CarModel.class);
		carModelRepository.put(carModel);

		FilterList filterList = new FilterList();
		filterList.addFilter(new PrefixFilter(Bytes.toBytes("GOL")));
		List<CarModel> carModelList = carModelRepository.scan(filterList);

		for (CarModel carModel1 : carModelList) {
			System.out.println(carModel1);
		}

		carModelRepository.delete("GOL#2002");

		System.out.println(Objects.isNull(carModelRepository.get("GOL#2002")));


	}

//	@Bean
//	public Connection connection() {
//		return MapHBContext.getConnection();
//	}

}


