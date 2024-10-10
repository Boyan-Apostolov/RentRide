package nl.fontys.s3.rentride_be.persistance.impl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.domain.car.CarFeatureType;
import nl.fontys.s3.rentride_be.persistance.CarFeatureRepository;
import nl.fontys.s3.rentride_be.persistance.entity.CarFeatureEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
@AllArgsConstructor
public class FakeCarFeatureRepository implements CarFeatureRepository {
    private static long NEXT_ID = 1;
    private final List<CarFeatureEntity> savedFeatures = new ArrayList<>();

    @Override
    public List<CarFeatureEntity> findAll() {
        return Collections.unmodifiableList(this.savedFeatures);
    }

    @Override
    public CarFeatureEntity save(CarFeatureEntity carFeatureEntity) {
        if (carFeatureEntity.getId() == null) {
            carFeatureEntity.setId(NEXT_ID);
            NEXT_ID++;
            this.savedFeatures.add(carFeatureEntity);
        }
        return carFeatureEntity;
    }

    @Override
    public CarFeatureEntity findById(long carFeatureId) {
        return this.savedFeatures
                .stream()
                .filter(carFeature -> carFeature.getId().equals(carFeatureId))  // Use equals for comparison
                .findFirst()
                .orElse(null);
    }

    @Override
    public int count() {
        return this.savedFeatures.size();
    }

    @Override
    public CarFeatureEntity findByFeatureText(String featureText) {
        return this.savedFeatures
                .stream()
                .filter(carFeature -> carFeature.getFeatureText().equals(featureText))  // Use equals for comparison
                .findFirst()
                .orElse(null);
    }

    @Override
    public CarFeatureEntity findByFeatureTextAndType(String currentRequestFeature, CarFeatureType featureType) {
        return this.savedFeatures
                .stream()
                .filter(carFeature -> carFeature.getFeatureText().equals(currentRequestFeature)
                        && carFeature.getFeatureType().equals(featureType))  // Use equals for comparison
                .findFirst()
                .orElse(null);
    }
}
