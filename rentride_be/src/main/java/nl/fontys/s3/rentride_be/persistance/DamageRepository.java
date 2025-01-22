package nl.fontys.s3.rentride_be.persistance;

import nl.fontys.s3.rentride_be.persistance.entity.DamageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DamageRepository extends JpaRepository<DamageEntity, Long> {
}
