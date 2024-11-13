package nl.fontys.s3.rentride_be.business.use_cases.discount;

public interface DeleteDiscountPlanPurchaseUseCase {
    void deleteDiscountPlanPurchase(Long userId, Long discountPlanId);
}
