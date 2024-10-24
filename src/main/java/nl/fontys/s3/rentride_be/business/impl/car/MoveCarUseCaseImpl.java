package nl.fontys.s3.rentride_be.business.impl.car;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.useCases.car.MoveCarUseCase;
import nl.fontys.s3.rentride_be.domain.car.Car;
import nl.fontys.s3.rentride_be.persistance.CarRepository;
import nl.fontys.s3.rentride_be.persistance.CityRepository;
import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MoveCarUseCaseImpl implements MoveCarUseCase {
    private CityRepository cityRepository;
    private CarRepository carRepository;
    @Override
    public void moveCar(Long carId, Long cityId) {
        Optional<CarEntity> car = carRepository.findById(carId);
        if(car.isEmpty()) throw new NotFoundException("MoveCar->Car");

        Optional<CityEntity> city = cityRepository.findById(cityId);
        if(city.isEmpty()) throw new NotFoundException("MoveCar->City");

        CarEntity foundCar = car.get();
        CityEntity foundCity = city.get();

        foundCar.setCity(foundCity);

        carRepository.save(foundCar);
    }
}
