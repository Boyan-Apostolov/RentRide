package nl.fontys.s3.rentride_be.domain.discount;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateDiscountPlanResponse {
    private Long discountPlanId;
}
