package nl.fontys.s3.rentride_be.domain.car;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.s3.rentride_be.persistance.entity.CarFeatureEntity;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
import org.springframework.format.annotation.NumberFormat;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCarRequest {
    @NotBlank
    @Size(min = 2, max = 50)
    private String make;

    @NotBlank
    @Size(min = 2, max = 50)
    private String model;

    @NotBlank
    @Size(min = 2, max = 10)
    private String registrationNumber;

    @NotEmpty
    private List<String> features;

    @NumberFormat
    @Min(1)
    @Max(20)
    private Double fuelConsumption;

    @NumberFormat
    @Min(1)
    private Long cityId;

    private List<String> photosBase64;

    private CityEntity foundCity;

    private List<CarFeatureEntity> foundFeatures;
}
