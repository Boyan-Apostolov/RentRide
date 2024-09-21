package nl.fontys.s3.rentride_be.domain.city;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private String name;

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
