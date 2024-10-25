package nl.fontys.s3.rentride_be.business.impl.car;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.AlreadyExistsException;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.use_cases.car.CreateCarUseCase;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional // Ensures the entire use case runs within a single transaction
public class CreateCarUseCaseImpl implements CreateCarUseCase {
    private final CarRepository carRepository;
    private final CityRepository cityRepository;
    private final CarFeatureRepository carFeatureRepository;

    @Override
    public CreateCarResponse createCar(CreateCarRequest request) {
        if (this.carRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {
            throw new AlreadyExistsException("Car");
        }

        CityEntity foundCity = prepareCarCity(request);
        List<CarFeatureEntity> foundFeatures = prepareCarFeatures(request);

        CarEntity savedCar = saveNewCar(request, foundCity, foundFeatures);

        return CreateCarResponse.builder()
                .carId(savedCar.getId())
                .build();
    }

    private CityEntity prepareCarCity(CreateCarRequest request) {
        return cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new NotFoundException("CreateCar->City"));
    }

    private List<CarFeatureEntity> prepareCarFeatures(CreateCarRequest request) {
        List<CarFeatureEntity> foundFeatures = new ArrayList<>();
        for (int i = 0; i < request.getFeatures().size(); i++) {
            String currentRequestFeature = request.getFeatures().get(i);
            CarFeatureType featureType = CarFeatureType.class.getEnumConstants()[i];

            CarFeatureEntity foundFeature = this.carFeatureRepository.findByFeatureTextAndFeatureType(currentRequestFeature, featureType);

            if (foundFeature == null) {
                foundFeature = this.carFeatureRepository.saveAndFlush(CarFeatureEntity.builder()
                        .featureText(currentRequestFeature)
                        .featureType(featureType)
                        .build());
            }

            foundFeatures.add(foundFeature);
        }
        return foundFeatures;
    }

    private CarEntity saveNewCar(CreateCarRequest request, CityEntity foundCity, List<CarFeatureEntity> foundFeatures) {
        CarEntity carEntity = CarEntity.builder()
                .make(request.getMake())
                .model(request.getModel())
                .registrationNumber(request.getRegistrationNumber())
                .fuelConsumption(request.getFuelConsumption())
                .city(foundCity)
                .photosBase64(request.getPhotosBase64())
                .features(new ArrayList<>())
                .build();

        for (CarFeatureEntity feature : foundFeatures) {
            CarFeatureEntity managedFeature = this.carFeatureRepository.saveAndFlush(feature);
            carEntity.getFeatures().add(managedFeature);
        }

        return this.carRepository.save(carEntity);
    }
}