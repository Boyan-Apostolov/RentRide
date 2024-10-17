package nl.fontys.s3.rentride_be.persistance;

import nl.fontys.s3.rentride_be.domain.car.CarFeatureType;
import nl.fontys.s3.rentride_be.persistance.entity.BookingEntity;
import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
import nl.fontys.s3.rentride_be.persistance.entity.CarFeatureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarFeatureRepository extends JpaRepository<CarFeatureEntity, Long> {
    CarFeatureEntity findByFeatureText(String featureText);

    CarFeatureEntity findByFeatureTextAndFeatureType(String currentRequestFeature, CarFeatureType featureType);
}
