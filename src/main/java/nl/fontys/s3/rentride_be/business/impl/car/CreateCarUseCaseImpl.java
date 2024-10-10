package nl.fontys.s3.rentride_be.business.impl.car;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.AlreadyExistsException;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.useCases.car.CreateCarUseCase;
import nl.fontys.s3.rentride_be.domain.car.CarFeature;
import nl.fontys.s3.rentride_be.domain.car.CarFeatureType;
import nl.fontys.s3.rentride_be.domain.car.CreateCarRequest;
import nl.fontys.s3.rentride_be.domain.car.CreateCarResponse;
import nl.fontys.s3.rentride_be.persistance.CarFeatureRepository;
import nl.fontys.s3.rentride_be.persistance.CarRepository;
import nl.fontys.s3.rentride_be.persistance.CityRepository;
import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
import nl.fontys.s3.rentride_be.persistance.entity.CarFeatureEntity;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@Service
@AllArgsConstructor
public class CreateCarUseCaseImpl implements CreateCarUseCase {
    private CarRepository carRepository;
    private CityRepository cityRepository;
    private CarFeatureRepository carFeatureRepository;

    @Override
    public CreateCarResponse createCar(CreateCarRequest request) {
        if (this.carRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {
            throw new AlreadyExistsException("Car");
        }

        prepareCarCity(request);
        prepareCarFeatures(request);

        CarEntity savedCar = saveNewCar(request);

        return CreateCarResponse.builder()
                .carId(savedCar.getId())
                .build();
    }

    private void prepareCarCity(CreateCarRequest request) {
        CityEntity foundCity = cityRepository.findById(request.getCityId());
        if(foundCity == null){
            throw new NotFoundException("CreateCar->City");
        }
        request.setFoundCity(foundCity);
    }

    private void prepareCarFeatures(CreateCarRequest request) {
        List<CarFeatureEntity> foundFeatures = new ArrayList<>();
        for(int i = 0; i < request.getFeatures().size(); i++) {
            String currentRequestFeature = request.getFeatures().get(i);
            CarFeatureType featureType = CarFeatureType.class.getEnumConstants()[i];

            CarFeatureEntity foundFeature = this.carFeatureRepository.findByFeatureTextAndType(currentRequestFeature, featureType);

            if(foundFeature == null){
                foundFeature = this.carFeatureRepository.save(CarFeatureEntity.builder()
                        .featureText(currentRequestFeature)
                        .featureType(featureType)
                        .build());
            }

            foundFeatures.add(foundFeature);
        }
        request.setFoundFeatures(foundFeatures);
    }

    private CarEntity saveNewCar(CreateCarRequest request) {
        CarEntity carEntity = CarEntity.builder()
                .make(request.getMake())
                .model(request.getModel())
                .registrationNumber(request.getRegistrationNumber())
                .fuelConsumption(request.getFuelConsumption())
                .city(request.getFoundCity())
                .photosBase64(request.getPhotosBase64())
                .features(request.getFoundFeatures())
                .build();

        return this.carRepository.save(carEntity);
    }
}
