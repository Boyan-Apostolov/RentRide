package nl.fontys.s3.rentride_be.business.impl.car;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.AlreadyExistsException;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.useCases.car.CreateCarUseCase;
import nl.fontys.s3.rentride_be.domain.car.CarFeatureType;
import nl.fontys.s3.rentride_be.domain.car.CreateCarRequest;
import nl.fontys.s3.rentride_be.domain.car.CreateCarResponse;
import nl.fontys.s3.rentride_be.persistance.CarRepository;
import nl.fontys.s3.rentride_be.persistance.CityRepository;
import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
import nl.fontys.s3.rentride_be.persistance.entity.CarFeatureEntity;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CreateCarUseCaseImpl implements CreateCarUseCase {
    private CarRepository carRepository;
    private CityRepository cityRepository;

    @Override
    public CreateCarResponse createCar(CreateCarRequest request) {
        if (this.carRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {
            throw new AlreadyExistsException("Car");
        }

        CityEntity foundCity = cityRepository.findById(request.getCityId());
        if(foundCity == null){
            throw new NotFoundException("CreateCar->City");
        }
        request.setFoundCity(foundCity);

        CarEntity savedCar = saveNewCar(request);

        //TODO: Save car features

        return CreateCarResponse.builder()
                .carId(savedCar.getId())
                .build();
    }

    private CarEntity saveNewCar(CreateCarRequest request) {
        CarEntity carEntity = CarEntity.builder()
                .make(request.getMake())
                .model(request.getModel())
                .registrationNumber(request.getRegistrationNumber())
                .fuelConsumption(request.getFuelConsumption())
                .city(request.getFoundCity())
                .photosBase64(request.getPhotosBase64())
                //TODO: Actually implement
                .features(List.of(
                        CarFeatureEntity.builder()
                                .id(1L)
                                .featureType(CarFeatureType.Seats)
                                .featureText("5")
                                .build(),
                        CarFeatureEntity.builder()
                                .id(2L)
                                .featureType(CarFeatureType.Doors)
                                .featureText("4")
                                .build(),
                        CarFeatureEntity.builder()
                                .id(3L)
                                .featureType(CarFeatureType.Transmission)
                                .featureText("Auto")
                                .build(),
                        CarFeatureEntity.builder()
                                .id(4L)
                                .featureType(CarFeatureType.Bonus)
                                .featureText("Heat")
                                .build()
                ))
                .build();

        return this.carRepository.save(carEntity);
    }
}
