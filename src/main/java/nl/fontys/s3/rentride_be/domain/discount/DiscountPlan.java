package nl.fontys.s3.rentride_be.domain.discount;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanPurchaseEntity;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscountPlan {
    private Long id;

    private String title;

    private String description;

    private Integer remainingUses;

    private Integer discountValue;

    private double price;

    private List<DiscountPlanPurchase> purchases;

}
