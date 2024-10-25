package nl.fontys.s3.rentride_be.persistance;

import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CarRepository extends JpaRepository<CarEntity, Long> {
    boolean existsByRegistrationNumber(String number);
}
