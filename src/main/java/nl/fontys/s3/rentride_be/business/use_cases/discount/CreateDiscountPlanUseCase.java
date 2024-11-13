package nl.fontys.s3.rentride_be.business.use_cases.discount;

import nl.fontys.s3.rentride_be.domain.discount.CreateDiscountPlanRequest;
import nl.fontys.s3.rentride_be.domain.discount.CreateDiscountPlanResponse;

public interface CreateDiscountPlanUseCase {
    CreateDiscountPlanResponse createDiscountPlan(CreateDiscountPlanRequest request);
}
