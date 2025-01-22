package nl.fontys.s3.rentride_be.business.use_cases.discount;

import nl.fontys.s3.rentride_be.domain.discount.CreateDiscountPaymentRequest;
import nl.fontys.s3.rentride_be.domain.discount.DiscountPlanPurchase;

public interface CreateDiscountPlanPurchaseUseCase {
    DiscountPlanPurchase createDiscountPlanPurchase(CreateDiscountPaymentRequest request);
}
