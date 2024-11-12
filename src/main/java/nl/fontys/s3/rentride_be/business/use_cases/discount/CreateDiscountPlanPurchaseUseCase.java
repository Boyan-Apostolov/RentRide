package nl.fontys.s3.rentride_be.business.use_cases.discount;

import nl.fontys.s3.rentride_be.domain.discount.CreateDiscountPaymentRequest;

public interface CreateDiscountPlanPurchaseUseCase {
    void createDiscountPlanPurchase(CreateDiscountPaymentRequest request);
}
