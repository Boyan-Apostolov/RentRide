package nl.fontys.s3.rentride_be.domain.discount;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateDiscountPaymentRequest {
    @NotNull
    private Long discountPlanId;
}
