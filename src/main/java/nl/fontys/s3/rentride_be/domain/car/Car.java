package nl.fontys.s3.rentride_be.domain.car;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import nl.fontys.s3.rentride_be.domain.city.City;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Car {
    private Long id;
    private String make;
    private String model;
    private String registrationNumber;
    private Integer seatsCount;
    private CarTransmissionType transmissionType;
    private Double fuelConsumption;
    private City city;
    private List<String> photosBase64;
}
