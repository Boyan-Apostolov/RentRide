package nl.fontys.s3.rentride_be.business.impl.car;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.useCases.car.GetCarsUseCase;
import nl.fontys.s3.rentride_be.domain.car.Car;
import nl.fontys.s3.rentride_be.persistance.CarRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class GetCarsUseCaseImpl implements GetCarsUseCase {
    private CarRepository carRepository;

    @Override
    public List<Car> getCars() {
        return this.carRepository
                .findAll()
                .stream()
                .map(CarConverter::convert)
                .toList();
    }
}
