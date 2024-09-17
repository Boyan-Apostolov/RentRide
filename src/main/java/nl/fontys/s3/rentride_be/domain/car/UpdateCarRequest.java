package nl.fontys.s3.rentride_be.domain.car;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
import org.springframework.format.annotation.NumberFormat;

import java.util.List;

@Data
@Builder
public class UpdateCarRequest {
    private Long id;

    @NotBlank
    private String make;

    @NotBlank
    private String model;

    @NotBlank
    private String registrationNumber;

    @NumberFormat
    @Min(1)
    @Max(7)
    private Integer seatsCount;

    @NumberFormat
    @Min(0)
    @Max(1)
    private CarTransmissionType transmissionType;

    @NumberFormat
    @Min(1)
    @Max(20)
    private Double fuelConsumption;

    @NumberFormat
    @Min(1)
    private Long cityId;

    private List<String> photosBase64;

    private CityEntity foundCity;
}
