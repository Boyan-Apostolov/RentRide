package nl.fontys.s3.rentride_be.persistance;


import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanPurchaseEntity;
import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanPurchaseKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DiscountPlanPurchaseRepository extends JpaRepository<DiscountPlanPurchaseEntity, DiscountPlanPurchaseKey> {
     Optional<DiscountPlanPurchaseEntity> findByUserIdAndDiscountPlanId(Long userId, Long discountPlanId);

     List<DiscountPlanPurchaseEntity> findAllByUserIdOrderByDiscountPlan_DiscountValueDesc(Long userId);

     List<DiscountPlanPurchaseEntity> findAllByRemainingUses(int remainingUses);

     @Query(value = "CALL get_most_bought_discount_plans()", nativeQuery = true)
     List<Object[]> getMostBoughtDiscountPlans();
}
