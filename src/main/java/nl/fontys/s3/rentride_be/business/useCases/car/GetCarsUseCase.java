package nl.fontys.s3.rentride_be.business.useCases.car;

import nl.fontys.s3.rentride_be.domain.car.Car;
import nl.fontys.s3.rentride_be.domain.city.City;

import java.util.List;

public interface GetCarsUseCase {
    List<Car> getCars();
}
