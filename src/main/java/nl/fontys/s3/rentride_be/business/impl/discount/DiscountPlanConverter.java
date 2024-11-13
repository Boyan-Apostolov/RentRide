package nl.fontys.s3.rentride_be.business.impl.discount;

import nl.fontys.s3.rentride_be.domain.discount.DiscountPlan;
import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanEntity;


public class DiscountPlanConverter {
    private DiscountPlanConverter() {}

    public static DiscountPlan convert(DiscountPlanEntity discountPlanEntity){
        if(discountPlanEntity == null) return null;
        return DiscountPlan.builder()
                .id(discountPlanEntity.getId())
                .title(discountPlanEntity.getTitle())
                .description(discountPlanEntity.getDescription())
                .remainingUses(discountPlanEntity.getRemainingUses())
                .discountValue(discountPlanEntity.getDiscountValue())
                .price(discountPlanEntity.getPrice())
//                .purchases(discountPlanEntity.getPurchases().stream().map(DiscountPlanPurchaseConverter::convert).toList())
                .build();
    }
}
