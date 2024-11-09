package nl.fontys.s3.rentride_be.business.use_cases.discount;

import nl.fontys.s3.rentride_be.domain.discount.DiscountPlan;

import java.util.List;

public interface GetAllDiscountPlansUseCase {
List<DiscountPlan> getAllDiscountPlans();
}
