package nl.fontys.s3.rentride_be.domain.city;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCityRequest {
    private Long id;

    @NotBlank
    private String name;

    @NumberFormat
    @Min(-90)
    @Max(90)
    private Double lat;

    @NumberFormat
    @Min(-180)
    @Max(180)
    private Double lon;
}
