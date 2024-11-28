package nl.fontys.s3.rentride_be.business.impl.discount;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.use_cases.auth.EmailerUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.discount.CreateDiscountPlanPurchaseUseCase;
import nl.fontys.s3.rentride_be.configuration.security.token.AccessToken;
import nl.fontys.s3.rentride_be.domain.discount.CreateDiscountPaymentRequest;
import nl.fontys.s3.rentride_be.domain.discount.DiscountPlanPurchase;
import nl.fontys.s3.rentride_be.domain.user.EmailType;
import nl.fontys.s3.rentride_be.persistance.DiscountPlanPurchaseRepository;
import nl.fontys.s3.rentride_be.persistance.DiscountPlanRepository;
import nl.fontys.s3.rentride_be.persistance.UserRepository;
import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanEntity;
import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanPurchaseEntity;
import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanPurchaseKey;
import nl.fontys.s3.rentride_be.persistance.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CreateDiscountPlanPurchaseUseCaseImpl implements CreateDiscountPlanPurchaseUseCase {
    private DiscountPlanPurchaseRepository discountPlanPurchaseRepository;
    private DiscountPlanRepository discountPlanRepository;
    private UserRepository userRepository;

    private AccessToken requestAccessToken;

    private EmailerUseCase emailerUseCase;

    @Override
    public DiscountPlanPurchase createDiscountPlanPurchase(CreateDiscountPaymentRequest request) {
        Long currentUserId = requestAccessToken.getUserId();

        Optional<DiscountPlanEntity> discountPlanOptional = this.discountPlanRepository.findById(request.getDiscountPlanId());
        if (discountPlanOptional.isEmpty()) throw new NotFoundException("CreateDiscountPlanPurchase->DiscountPlan");
        DiscountPlanEntity discountPlanEntity = discountPlanOptional.get();

        Optional<UserEntity> optionalUser = this.userRepository.findById(currentUserId);
        if (optionalUser.isEmpty()) throw new NotFoundException("CreateDiscountPlanPurchase->User");
        UserEntity userEntity = optionalUser.get();

        DiscountPlanPurchaseEntity purchaseEntity = DiscountPlanPurchaseEntity.builder()
                .discountPlan(discountPlanEntity)
                .user(userEntity)
                .purchaseDate(LocalDateTime.now())
                .remainingUses(0)
                .id(DiscountPlanPurchaseKey.builder()
                        .discountPlanId(discountPlanEntity.getId())
                        .userId(userEntity.getId())
                        .build())
                .build();

        discountPlanPurchaseRepository.save(purchaseEntity);

        emailerUseCase.send(userEntity.getEmail(), "Discount plan purchased successfully",
                String.format("We want to inform to that your purchase of \" %s \" has been successful! During your next booking, the discount plan with the highest discount value will be automatically applied",
                        discountPlanEntity.getTitle()),
                EmailType.PROMO);

        return DiscountPlanPurchaseConverter.convert(purchaseEntity);

    }
}
