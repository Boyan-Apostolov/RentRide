package nl.fontys.s3.rentride_be.business.impl.car;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.AlreadyExistsException;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.useCases.car.CreateCarUseCase;
import nl.fontys.s3.rentride_be.domain.car.CarTransmissionType;
import nl.fontys.s3.rentride_be.domain.car.CreateCarRequest;
import nl.fontys.s3.rentride_be.domain.car.CreateCarResponse;
import nl.fontys.s3.rentride_be.domain.city.CreateCityRequest;
import nl.fontys.s3.rentride_be.domain.city.CreateCityResponse;
import nl.fontys.s3.rentride_be.persistance.CarRepository;
import nl.fontys.s3.rentride_be.persistance.CityRepository;
import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
import org.springframework.stereotype.Service;

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
                .seatsCount(request.getSeatsCount())
                .transmissionType(CarTransmissionType.values()[request.getTransmissionType()])
                .city(request.getFoundCity())
                .build();

        return this.carRepository.save(carEntity);
    }
}
