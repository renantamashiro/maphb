package com.example.demoservice.model;

import com.maphb.annotations.Column;
import com.maphb.annotations.RowKey;
import com.maphb.annotations.Table;

@Table(name = "new_table", columnFamilies = {"abc", "def", "opa", "newcf"})
public class CarModel {

    @RowKey(keys = {"brand", "yearModel"}, delimiter = "#")
    private String rowKey;

    @Column(family = "abc", qualifier = "BRAND")
    private String brand;

    @Column(family = "abc", qualifier = "MODEL_YEAR")
    private String yearModel;

    public CarModel() {}

    public CarModel(String brand, String yearModel) {
        this.brand = brand;
        this.yearModel = yearModel;
    }

    @Override
    public String toString() {
        return "Car model " + this.brand + " and " + this.yearModel + " year";
    }

    public String getRowKey() {
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getYearModel() {
        return yearModel;
    }

    public void setYearModel(String yearModel) {
        this.yearModel = yearModel;
    }

}
