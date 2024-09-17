package nl.fontys.s3.rentride_be.business.useCases.car;

import nl.fontys.s3.rentride_be.domain.car.Car;

public interface DeleteCarUseCase {
    void deleteCar(Long carId);
}
