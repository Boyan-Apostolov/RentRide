package nl.fontys.s3.rentride_be.business.impl.discount;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.use_cases.auth.EmailerUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.discount.DeleteDiscountPlanPurchaseUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.discount.UpdateDiscountPlanPurchaseUseCase;
import nl.fontys.s3.rentride_be.configuration.security.token.AccessToken;
import nl.fontys.s3.rentride_be.domain.discount.UpdateDiscountPaymentRequest;
import nl.fontys.s3.rentride_be.domain.user.EmailType;
import nl.fontys.s3.rentride_be.persistance.DiscountPlanPurchaseRepository;
import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanPurchaseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateDiscountPlanPurchaseUseCaseImpl implements UpdateDiscountPlanPurchaseUseCase {
    private DiscountPlanPurchaseRepository discountPlanPurchaseRepository;
    private DeleteDiscountPlanPurchaseUseCase deleteDiscountPlanPurchaseUseCase;
    private AccessToken requestAccessToken;
    private EmailerUseCase emailerUseCase;

    @Override
    public void updateDiscountPlanPurchase(UpdateDiscountPaymentRequest request) {
        Long currentUserId = requestAccessToken.getUserId();
        Optional<DiscountPlanPurchaseEntity> optionalDiscountPlanPurchaseEntity = discountPlanPurchaseRepository.findByUserIdAndDiscountPlanId(currentUserId, request.getDiscountPlanId());
        if (optionalDiscountPlanPurchaseEntity.isEmpty())
            throw new NotFoundException("UpdateDiscountPurchase->DiscountPlan");
        DiscountPlanPurchaseEntity discountPlanPurchaseEntity = optionalDiscountPlanPurchaseEntity.get();

        if (request.getRemainingUses() == 0) {
            deleteDiscountPlanPurchaseUseCase.deleteDiscountPlanPurchase(currentUserId, request.getDiscountPlanId());

            emailerUseCase.send(discountPlanPurchaseEntity.getUser().getEmail(), "Discount plan expired!",
                    String.format("We would like to inform you that your discount plan \" %s \" has just expired and cannot be used anymore for future bookings. If you still want to receive discounts, please navigate to the Discounts page and purchase a new discount!",
                            discountPlanPurchaseEntity.getDiscountPlan().getTitle()),
                    EmailType.PROMO);

        } else {
            discountPlanPurchaseEntity.setRemainingUses(request.getRemainingUses());

            discountPlanPurchaseRepository.save(discountPlanPurchaseEntity);
        }
    }
}
