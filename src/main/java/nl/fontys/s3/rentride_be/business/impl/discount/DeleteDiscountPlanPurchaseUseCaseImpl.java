package nl.fontys.s3.rentride_be.business.impl.discount;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.use_cases.discount.DeleteDiscountPlanPurchaseUseCase;
import nl.fontys.s3.rentride_be.persistance.DiscountPlanPurchaseRepository;
import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanPurchaseEntity;
import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanPurchaseKey;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DeleteDiscountPlanPurchaseUseCaseImpl implements DeleteDiscountPlanPurchaseUseCase {
    private DiscountPlanPurchaseRepository purchaseRepository;
    @Transactional
    @Override
    public void deleteDiscountPlanPurchase(Long userId, Long discountPlanId) {
        Optional<DiscountPlanPurchaseEntity> purchase = purchaseRepository.findByUserIdAndDiscountPlanId(userId, discountPlanId);
        if(purchase.isEmpty()) throw new NotFoundException("DeleteDiscount->Entity");

        purchaseRepository.delete(purchase.get());
    }
}
