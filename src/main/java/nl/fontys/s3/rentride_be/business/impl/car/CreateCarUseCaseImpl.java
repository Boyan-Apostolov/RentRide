package nl.fontys.s3.rentride_be.business.impl.car;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.AlreadyExistsException;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.useCases.car.CreateCarUseCase;
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
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional // Ensures the entire use case runs within a single transaction
public class CreateCarUseCaseImpl implements CreateCarUseCase {
    private final CarRepository carRepository;
    private final CityRepository cityRepository;
    private final CarFeatureRepository carFeatureRepository;

    @Override
    public CreateCarResponse createCar(CreateCarRequest request) {
        // Check if a car with the given registration number already exists
        if (this.carRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {
            throw new AlreadyExistsException("Car");
        }

        // Prepare city and features for the car
        CityEntity foundCity = prepareCarCity(request);
        List<CarFeatureEntity> foundFeatures = prepareCarFeatures(request);

        // Save the new car
        CarEntity savedCar = saveNewCar(request, foundCity, foundFeatures);

        // Return the response with the car ID
        return CreateCarResponse.builder()
                .carId(savedCar.getId())
                .build();
    }

    private CityEntity prepareCarCity(CreateCarRequest request) {
        // Find the city by ID, throw an exception if not found
        return cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new NotFoundException("CreateCar->City"));
    }

    private List<CarFeatureEntity> prepareCarFeatures(CreateCarRequest request) {
        List<CarFeatureEntity> foundFeatures = new ArrayList<>();
        // Iterate through each feature in the request
        for (int i = 0; i < request.getFeatures().size(); i++) {
            String currentRequestFeature = request.getFeatures().get(i);
            CarFeatureType featureType = CarFeatureType.class.getEnumConstants()[i];

            // Find the feature by text and type
            CarFeatureEntity foundFeature = this.carFeatureRepository.findByFeatureTextAndFeatureType(currentRequestFeature, featureType);

            // If feature doesn't exist, save a new feature
            if (foundFeature == null) {
                foundFeature = this.carFeatureRepository.saveAndFlush(CarFeatureEntity.builder()
                        .featureText(currentRequestFeature)
                        .featureType(featureType)
                        .build());
            }

            // Add the managed feature to the list
            foundFeatures.add(foundFeature);
        }
        return foundFeatures;
    }

    private CarEntity saveNewCar(CreateCarRequest request, CityEntity foundCity, List<CarFeatureEntity> foundFeatures) {
        // Create a new CarEntity with the request and prepared data
        CarEntity carEntity = CarEntity.builder()
                .make(request.getMake())
                .model(request.getModel())
                .registrationNumber(request.getRegistrationNumber())
                .fuelConsumption(request.getFuelConsumption())
                .city(foundCity)
                .photosBase64(request.getPhotosBase64())
                .features(new ArrayList<>()) // Initialize with an empty list of features
                .build();

        // Ensure all features are managed by saving and flushing before adding them to the car
        for (CarFeatureEntity feature : foundFeatures) {
            CarFeatureEntity managedFeature = this.carFeatureRepository.saveAndFlush(feature);
            carEntity.getFeatures().add(managedFeature);
        }

        // Save the car entity
        return this.carRepository.save(carEntity);
    }
}