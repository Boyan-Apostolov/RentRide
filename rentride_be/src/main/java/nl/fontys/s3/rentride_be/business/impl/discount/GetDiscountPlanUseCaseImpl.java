package nl.fontys.s3.rentride_be.business.impl.discount;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.discount.GetDiscountPlanUseCase;
import nl.fontys.s3.rentride_be.domain.discount.DiscountPlan;
import nl.fontys.s3.rentride_be.persistance.DiscountPlanRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

public class GetDiscountPlanUseCaseImpl implements GetDiscountPlanUseCase {
private DiscountPlanRepository discountPlanRepository;
    @Override
    public DiscountPlan getDiscountPlan(Long planId) {
        return DiscountPlanConverter.convert(
                discountPlanRepository.findById(planId).orElse(null)
        );
    }
}
