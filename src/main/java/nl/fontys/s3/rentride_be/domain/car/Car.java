package nl.fontys.s3.rentride_be.domain.car;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.s3.rentride_be.domain.city.City;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Car {
    private Long id;
    private String make;
    private String model;
    private String registrationNumber;
    private List<CarFeature> carFeatures;
    private Double fuelConsumption;
    private City city;
    private List<String> photosBase64;
    private Double fuelPrice;
}
