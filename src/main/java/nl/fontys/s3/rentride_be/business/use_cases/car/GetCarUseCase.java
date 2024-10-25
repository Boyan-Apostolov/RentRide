package nl.fontys.s3.rentride_be.business.use_cases.car;

import nl.fontys.s3.rentride_be.domain.car.Car;

public interface    GetCarUseCase {
    Car getCar(Long carId);
}
