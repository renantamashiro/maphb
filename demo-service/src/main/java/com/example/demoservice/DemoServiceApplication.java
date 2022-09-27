package com.example.demoservice;

import com.maphb.MapHBContext;
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
		SpringApplication.run(DemoServiceApplication.class, args);
	}

	@Bean
	public Connection connection() {
		return MapHBContext.getConnection();
	}

}


