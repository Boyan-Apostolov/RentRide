package nl.fontys.s3.rentride_be.business.useCases.user;

import nl.fontys.s3.rentride_be.domain.car.Car;

public interface GetCarUseCase {
    Car getCar(Long carId);
}
