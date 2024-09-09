package nl.fontys.s3.rentride_be.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Car {
    private String make;
    private String model;
    private String registrationNumber;
    private Integer seatsCount;
    private CarTransmissionType transmissionType;
    private Double fuelConsumption;
    private City city;
    //Photos??
}
