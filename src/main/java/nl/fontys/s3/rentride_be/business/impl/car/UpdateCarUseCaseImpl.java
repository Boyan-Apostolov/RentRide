package nl.fontys.s3.rentride_be.business.impl.car;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.useCases.car.UpdateCarUseCase;
import nl.fontys.s3.rentride_be.domain.car.UpdateCarRequest;
import nl.fontys.s3.rentride_be.persistance.CarRepository;
import nl.fontys.s3.rentride_be.persistance.CityRepository;
import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdateCarUseCaseImpl implements UpdateCarUseCase {
    private CarRepository carRepository;
    private CityRepository cityRepository;

    @Override
    public void updateCar(UpdateCarRequest request) {
        verifyCarExists(request.getId());

        CityEntity cityOptional = this.cityRepository.findById(request.getCityId());
        if (cityOptional == null) {
            throw new NotFoundException("Update->Car->City");
        }
        request.setFoundCity(cityOptional);

        updateEntity(request);
    }

    private void verifyCarExists(Long carId){
        CarEntity carOptional = this.carRepository.findById(carId);
        if (carOptional == null) {
            throw new NotFoundException("Update->Car");
        }
    }

    private void updateEntity(UpdateCarRequest request) {
        CarEntity carEntity = this.carRepository.findById(request.getId());

        carEntity.setMake(request.getMake());
        carEntity.setModel(request.getModel());
        carEntity.setRegistrationNumber(request.getRegistrationNumber());
        carEntity.setSeatsCount(request.getSeatsCount());
        carEntity.setFuelConsumption(request.getFuelConsumption());
        carEntity.setTransmissionType(request.getTransmissionType());
        carEntity.setCity(request.getFoundCity());


        this.carRepository.save(carEntity);
    }
}
