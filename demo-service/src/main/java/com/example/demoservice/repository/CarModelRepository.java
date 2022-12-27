package com.example.demoservice.repository;

import com.example.demoservice.model.CarModel;
import com.maphb.manager.GenericRepository;

public class CarModelRepository extends GenericRepository<CarModel> {
    public CarModelRepository(Class<CarModel> clazz) {
        super(clazz);
    }
}
