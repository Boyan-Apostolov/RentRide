package nl.fontys.s3.rentride_be.persistance;

import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountPlanRepository extends JpaRepository<DiscountPlanEntity, Long> {
}
