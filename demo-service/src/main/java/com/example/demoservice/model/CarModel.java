package com.example.demoservice.model;

import com.maphb.annotations.Column;
import com.maphb.annotations.Table;

@Table(name = "new_table", columnFamilies = {"abc", "def", "opa", "newcf"})
public class CarModel {

    private Integer doors;
    @Column(family = "abc", qualifier = "GORDURA")
    private String engineType;
    private Integer wheels;

    public CarModel() {}

    public CarModel(Integer doors, String engineType, Integer wheels) {
        this.doors = doors;
        this.engineType = engineType;
        this.wheels = wheels;
    }

    @Override
    public String toString() {
        return "Car with engine " + this.engineType + " and " + this.wheels + " wheels and " + this.doors + " doors.";
    }

    public void setEngineType(String value) {
        this.engineType = value;
    }
}
