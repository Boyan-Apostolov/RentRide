package nl.fontys.s3.rentride_be.persistance;

import nl.fontys.s3.rentride_be.persistance.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
}
