package nl.fontys.s3.rentride_be.business.impl.car;

import nl.fontys.s3.rentride_be.business.impl.city.CityConverter;
import nl.fontys.s3.rentride_be.domain.car.Car;
import nl.fontys.s3.rentride_be.domain.car.CarFeature;
import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;

public final class CarConverter {
    private CarConverter() {}

    public static Car convert(CarEntity carEntity){
        if(carEntity == null) return null;
        return Car.builder()
                .id(carEntity.getId())
                .make(carEntity.getMake())
                .model(carEntity.getModel())
                .registrationNumber(carEntity.getRegistrationNumber())
                .fuelConsumption(carEntity.getFuelConsumption())
                .city(CityConverter.convert(carEntity.getCity()))
                .photosBase64(carEntity.getPhotosBase64())
                .carFeatures(carEntity.getFeatures().stream().map(CarFeatureConverter::convert).toList())
                .build();
    }
}
