package nl.fontys.s3.rentride_be.business.impl.discount;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.discount.CreateDiscountPlanUseCase;
import nl.fontys.s3.rentride_be.domain.discount.CreateDiscountPlanRequest;
import nl.fontys.s3.rentride_be.domain.discount.CreateDiscountPlanResponse;
import nl.fontys.s3.rentride_be.persistance.DiscountPlanRepository;
import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanEntity;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CreateDiscountPlanUseCaseImpl implements CreateDiscountPlanUseCase {
    private DiscountPlanRepository discountPlanRepository;

    @Override
    public CreateDiscountPlanResponse createDiscountPlan(CreateDiscountPlanRequest request) {
        DiscountPlanEntity discountPlanEntity = DiscountPlanEntity.builder()
                .title(request.getTitle())
                .discountValue(request.getDiscountValue())
                .description(request.getDescription())
                .remainingUses(request.getRemainingUses())
                .price(request.getPrice())
                .build();

        DiscountPlanEntity savedEntity = discountPlanRepository.save(discountPlanEntity);

        return CreateDiscountPlanResponse.builder()
                .discountPlanId(savedEntity.getId())
                .build();
    }
}
