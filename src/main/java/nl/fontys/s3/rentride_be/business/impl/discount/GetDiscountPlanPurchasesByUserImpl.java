package nl.fontys.s3.rentride_be.business.impl.discount;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.discount.GetDiscountPlanPurchasesByUser;
import nl.fontys.s3.rentride_be.domain.discount.DiscountPlanPurchase;
import nl.fontys.s3.rentride_be.persistance.DiscountPlanPurchaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetDiscountPlanPurchasesByUserImpl implements GetDiscountPlanPurchasesByUser {
private DiscountPlanPurchaseRepository discountPlanPurchaseRepository;

    @Override
    public List<DiscountPlanPurchase> getDiscountPlanPurchasesByUser(Long userId) {
        Long currentUserId = 1L;

        if(userId == null){
            userId = currentUserId;
        }
        return
                this.discountPlanPurchaseRepository
                        .findAllByUserIdOrderByRemainingUsesDesc(userId)
                        .stream().map(DiscountPlanPurchaseConverter::convert)
                        .toList();
    }
}

