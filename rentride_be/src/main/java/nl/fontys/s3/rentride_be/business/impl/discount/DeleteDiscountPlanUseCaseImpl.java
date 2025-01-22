package nl.fontys.s3.rentride_be.business.impl.discount;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.use_cases.discount.DeleteDiscountPlanUseCase;
import nl.fontys.s3.rentride_be.persistance.DiscountPlanRepository;
import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DeleteDiscountPlanUseCaseImpl implements DeleteDiscountPlanUseCase {
    private DiscountPlanRepository discountPlanRepository;
    @Override
    public void deleteDiscountPlan(Long discountPlanId) {
        Optional<DiscountPlanEntity> optionalDiscountPlanEntity = discountPlanRepository.findById(discountPlanId);
        if (optionalDiscountPlanEntity.isEmpty()) throw new NotFoundException("Delete->DiscountPlan");

        DiscountPlanEntity discountPlanEntity = optionalDiscountPlanEntity.get();
        discountPlanRepository.delete(discountPlanEntity);
    }
}
