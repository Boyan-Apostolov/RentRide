package nl.fontys.s3.rentride_be.domain.discount;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.NumberFormat;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateDiscountPlanRequest {
    @NotNull
    @Size(min = 3, max = 255)
    private String title;

    @NotNull
    @Size(min = 3, max = 255)
    private String description;

    @NotNull
    @NumberFormat
    @Min(1)
    private Integer remainingUses;

    @NotNull
    @NumberFormat
    @Min(1)
    @Max(100)
    private Integer discountValue;

    @NotNull
    @NumberFormat
    @Min(1)
    private double price;
}
