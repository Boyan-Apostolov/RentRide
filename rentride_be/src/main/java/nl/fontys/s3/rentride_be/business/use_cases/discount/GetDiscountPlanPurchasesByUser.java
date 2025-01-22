package nl.fontys.s3.rentride_be.business.use_cases.discount;

import nl.fontys.s3.rentride_be.domain.discount.DiscountPlanPurchase;

import java.util.List;

public interface GetDiscountPlanPurchasesByUser {
    List<DiscountPlanPurchase> getDiscountPlanPurchasesByUser(Long userId);
}
