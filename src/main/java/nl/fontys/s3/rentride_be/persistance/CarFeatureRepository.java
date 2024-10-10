package nl.fontys.s3.rentride_be.persistance;

import nl.fontys.s3.rentride_be.domain.car.CarFeatureType;
import nl.fontys.s3.rentride_be.persistance.entity.BookingEntity;
import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
import nl.fontys.s3.rentride_be.persistance.entity.CarFeatureEntity;

import java.util.List;

public interface CarFeatureRepository {
    List<CarFeatureEntity> findAll();

    CarFeatureEntity save(CarFeatureEntity carFeatureEntity);

    CarFeatureEntity findById(long carFeatureId);

    int count();

    CarFeatureEntity findByFeatureText(String featureText);

    CarFeatureEntity findByFeatureTextAndType(String currentRequestFeature, CarFeatureType featureType);
}
