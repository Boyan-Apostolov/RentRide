package nl.fontys.s3.rentride_be.domain.payment;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreatePaymentRequest {
    @NotBlank
    private String description;

    private double totalCost;

    private Long userId;
}
