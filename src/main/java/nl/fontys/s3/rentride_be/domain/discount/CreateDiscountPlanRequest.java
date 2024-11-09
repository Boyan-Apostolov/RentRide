package nl.fontys.s3.rentride_be.domain.discount;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateDiscountPlanRequest {
    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    @NumberFormat
    @Min(1)
    private Integer remainingUses;

    @NotNull
    @NumberFormat
    @Min(1)
    private Integer discountValue;

    @NotNull
    @NumberFormat
    @Min(1)
    private double price;
}
