package nl.fontys.s3.rentride_be.business.use_cases.car;

import nl.fontys.s3.rentride_be.domain.car.UpdateCarRequest;

public interface UpdateCarUseCase {
    void updateCar(UpdateCarRequest request);
}
