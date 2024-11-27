package nl.fontys.s3.rentride_be.business.impl.discount;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.discount.IsDiscountPlanBoughtUseCase;
import nl.fontys.s3.rentride_be.configuration.security.token.AccessToken;
import nl.fontys.s3.rentride_be.domain.discount.IsDiscountPlanBoughtResponse;
import nl.fontys.s3.rentride_be.persistance.DiscountPlanPurchaseRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class IsDiscountPlanBoughtUseCaseImpl implements IsDiscountPlanBoughtUseCase {
    private DiscountPlanPurchaseRepository discountPlanPurchaseRepository;
    private AccessToken requestAccessToken;
    @Override
    public IsDiscountPlanBoughtResponse isDiscountPlanBought(Long discountPlanId) {
boolean isBought = discountPlanPurchaseRepository
        .findByUserIdAndDiscountPlanId(requestAccessToken.getUserId(), discountPlanId)
        .isPresent();

        return IsDiscountPlanBoughtResponse.builder()
                .isBought(isBought)
                .build();
    }
}
