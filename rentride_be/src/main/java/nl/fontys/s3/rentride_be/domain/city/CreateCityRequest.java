package nl.fontys.s3.rentride_be.domain.city;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCityRequest {
    @NotBlank
    @Size(min = 1, max = 255)
    private String name;

    @NotBlank
    @Size(min = 1, max = 255)
    private String depoAddress;

    @NotNull
    @NumberFormat
    @Min(-90)
    @Max(90)
    private Double lat;

    @NotNull
    @NumberFormat
    @Min(-180)
    @Max(180)
    private Double lon;
}
