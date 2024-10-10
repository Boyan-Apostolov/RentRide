package nl.fontys.s3.rentride_be.business.impl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.impl.car.CarFeatureConverter;
import nl.fontys.s3.rentride_be.business.useCases.car.GetAllCarFeatures;
import nl.fontys.s3.rentride_be.domain.car.CarFeature;
import nl.fontys.s3.rentride_be.persistance.CarFeatureRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetAllCarFeaturesImpl implements GetAllCarFeatures {
    private CarFeatureRepository carFeatureRepository;

    @Override
    public List<CarFeature> getAllCarFeatures() {
        return carFeatureRepository.findAll().stream().map(CarFeatureConverter::convert).toList();
    }
}
