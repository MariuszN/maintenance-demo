package pl.nurkowski.maintenance.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CarsDto {
    private String model;
    private String brand;

    public CarsDto(String model, String brand) {
        this.model = model;
        this.brand = brand;
    }

    public CarsDto() {
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
