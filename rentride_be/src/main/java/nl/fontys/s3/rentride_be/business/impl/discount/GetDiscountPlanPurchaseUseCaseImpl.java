package nl.fontys.s3.rentride_be.business.impl.discount;

import lombok.RequiredArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.discount.GetDiscountPlanPurchaseUseCase;
import nl.fontys.s3.rentride_be.configuration.security.token.AccessToken;
import nl.fontys.s3.rentride_be.domain.discount.DiscountPlanPurchase;
import nl.fontys.s3.rentride_be.persistance.DiscountPlanPurchaseRepository;
import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanPurchaseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetDiscountPlanPurchaseUseCaseImpl implements GetDiscountPlanPurchaseUseCase {
private final DiscountPlanPurchaseRepository discountPlanPurchaseRepository;
private final AccessToken requestAccessToken;

    @Override
    public DiscountPlanPurchase getDiscountPlanPurchaseByCurrentUserAndDiscountId(Long discountId) {
        DiscountPlanPurchaseEntity discountPlanPurchaseEntity = this.discountPlanPurchaseRepository
                .findByUserIdAndDiscountPlanId(requestAccessToken.getUserId(), discountId)
                .orElse(null);
        return  DiscountPlanPurchaseConverter.convert(discountPlanPurchaseEntity);
    }
}
