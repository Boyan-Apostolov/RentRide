package nl.fontys.s3.rentride_be.business.impl.car;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.useCases.car.GetCarUseCase;
import nl.fontys.s3.rentride_be.business.useCases.car.GetCarsUseCase;
import nl.fontys.s3.rentride_be.business.useCases.city.GetRouteBetweenCitiesUseCase;
import nl.fontys.s3.rentride_be.domain.car.Car;
import nl.fontys.s3.rentride_be.domain.city.GetRouteResponse;
import nl.fontys.s3.rentride_be.persistance.CarRepository;
import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetCarUseCaseImpl implements GetCarUseCase {
    private CarRepository carRepository;


    @Override
    public Car getCar(Long carId) {

        return CarConverter.convert(
                this.carRepository.findById(carId).orElse(null)
        );
    }
}
