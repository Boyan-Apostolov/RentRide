package nl.fontys.s3.rentride_be.business.impl.discount;


import nl.fontys.s3.rentride_be.business.impl.user.UserConverter;
import nl.fontys.s3.rentride_be.domain.discount.DiscountPlanPurchase;
import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanPurchaseEntity;

public class DiscountPlanPurchaseConverter {
    private DiscountPlanPurchaseConverter() {}

    public static DiscountPlanPurchase convert(DiscountPlanPurchaseEntity discountPlanPurchaseEntity){
        if(discountPlanPurchaseEntity == null) return null;
        return DiscountPlanPurchase.builder()
                .user(UserConverter.convert(discountPlanPurchaseEntity.getUser()))
                .discountPlan(DiscountPlanConverter.convert(discountPlanPurchaseEntity.getDiscountPlan()))
                .purchaseDate(discountPlanPurchaseEntity.getPurchaseDate())
                .remainingUses(discountPlanPurchaseEntity.getRemainingUses())
                .build();
    }
}
