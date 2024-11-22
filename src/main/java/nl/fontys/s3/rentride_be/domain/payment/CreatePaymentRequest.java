package nl.fontys.s3.rentride_be.domain.payment;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentRequest {
    @NotBlank
    @Size(min = 3, max = 255)
    private String description;

    @Min(0)
    private double totalCost;

    private Long userId;
}
