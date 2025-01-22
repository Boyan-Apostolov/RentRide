package nl.fontys.s3.rentride_be.business.impl.discount;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.discount.GetAllDiscountPlansUseCase;
import nl.fontys.s3.rentride_be.domain.discount.DiscountPlan;
import nl.fontys.s3.rentride_be.persistance.DiscountPlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetAllDiscountPlansUseCaseImpl implements GetAllDiscountPlansUseCase {
private DiscountPlanRepository discountPlanRepository;


    @Override
    public List<DiscountPlan> getAllDiscountPlans() {
        return discountPlanRepository
                .findAll()
                .stream().map(DiscountPlanConverter::convert)
                .toList();
    }
}
