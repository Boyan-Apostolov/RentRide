package nl.fontys.s3.rentride_be.business.impl.discount;

import lombok.RequiredArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.discount.GetDiscountPlanPurchaseUseCase;
import nl.fontys.s3.rentride_be.domain.discount.DiscountPlanPurchase;
import nl.fontys.s3.rentride_be.persistance.DiscountPlanPurchaseRepository;
import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanPurchaseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetDiscountPlanPurchaseUseCaseImpl implements GetDiscountPlanPurchaseUseCase {
private Long currentUserId = 1L;
private final DiscountPlanPurchaseRepository discountPlanPurchaseRepository;

    @Override
    public DiscountPlanPurchase getDiscountPlanPurchaseByCurrentUserAndDiscountId(Long discountId) {
        DiscountPlanPurchaseEntity discountPlanPurchaseEntity = this.discountPlanPurchaseRepository
                .findByUserIdAndDiscountPlanId(currentUserId, discountId)
                .orElse(null);
        return  DiscountPlanPurchaseConverter.convert(discountPlanPurchaseEntity);
    }
}
