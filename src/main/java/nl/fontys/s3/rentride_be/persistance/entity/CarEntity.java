package nl.fontys.s3.rentride_be.persistance.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CarEntity {
    private Long id;
    private String make;
    private String model;
    private String registrationNumber;
    private List<CarFeatureEntity> features;
    private Double fuelConsumption;
    private CityEntity city;
    private List<String> photosBase64;
}
