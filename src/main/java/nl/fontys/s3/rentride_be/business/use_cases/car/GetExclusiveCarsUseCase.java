package nl.fontys.s3.rentride_be.business.use_cases.car;

import nl.fontys.s3.rentride_be.domain.car.Car;

import java.util.List;

public interface GetExclusiveCarsUseCase {
    List<Car> getCars();
}
