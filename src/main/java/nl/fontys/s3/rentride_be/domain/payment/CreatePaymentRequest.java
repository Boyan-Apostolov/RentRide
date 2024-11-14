package nl.fontys.s3.rentride_be.domain.payment;

import jakarta.validation.constraints.NotBlank;
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
    private String description;

    private double totalCost;

    private Long userId;
}
