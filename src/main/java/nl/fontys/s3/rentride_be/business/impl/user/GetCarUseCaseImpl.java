package nl.fontys.s3.rentride_be.business.impl.user;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.useCases.car.GetCarUseCase;
import nl.fontys.s3.rentride_be.domain.car.Car;
import nl.fontys.s3.rentride_be.persistance.CarRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetCarUseCaseImpl implements GetCarUseCase {
    private CarRepository carRepository;

    @Override
    public Car getCar(Long carId) {
        return UserConverter.convert(this.carRepository.findById(carId));
    }
}
