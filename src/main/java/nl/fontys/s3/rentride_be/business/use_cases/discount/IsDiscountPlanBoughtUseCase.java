package nl.fontys.s3.rentride_be.business.use_cases.discount;

import nl.fontys.s3.rentride_be.domain.discount.IsDiscountPlanBoughtResponse;

public interface IsDiscountPlanBoughtUseCase {

    IsDiscountPlanBoughtResponse isDiscountPlanBought(Long discountPlanId);
}
