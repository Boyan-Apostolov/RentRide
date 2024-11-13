package nl.fontys.s3.rentride_be.domain.discount;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDiscountPaymentRequest {
    private Long discountPlanId;
    private Integer remainingUses;
}
