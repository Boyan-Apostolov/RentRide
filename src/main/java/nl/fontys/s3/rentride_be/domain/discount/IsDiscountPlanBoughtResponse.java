package nl.fontys.s3.rentride_be.domain.discount;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class IsDiscountPlanBoughtResponse {
    private boolean isBought;
}
