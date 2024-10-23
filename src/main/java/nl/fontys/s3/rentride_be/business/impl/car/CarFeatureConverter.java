package nl.fontys.s3.rentride_be.business.impl.car;

import nl.fontys.s3.rentride_be.domain.car.CarFeature;
import nl.fontys.s3.rentride_be.persistance.entity.CarFeatureEntity;

public final class CarFeatureConverter {
    private CarFeatureConverter() {}

    public static CarFeature convert(CarFeatureEntity featureEntity){
        return CarFeature.builder()
                .id(featureEntity.getId())
                .featureType(featureEntity.getFeatureType())
                .featureText(featureEntity.getFeatureText())
                .build();
    }
}