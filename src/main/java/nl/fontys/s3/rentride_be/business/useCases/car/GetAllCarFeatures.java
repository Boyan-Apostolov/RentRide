package nl.fontys.s3.rentride_be.business.useCases.car;

import nl.fontys.s3.rentride_be.domain.car.CarFeature;

import java.util.List;

public interface GetAllCarFeatures {
    public List<CarFeature> getAllCarFeatures();
}
