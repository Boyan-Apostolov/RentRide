package nl.fontys.s3.rentride_be.business.use_cases.car;

import nl.fontys.s3.rentride_be.domain.car.CreateCarRequest;
import nl.fontys.s3.rentride_be.domain.car.CreateCarResponse;

public interface CreateCarUseCase {
    CreateCarResponse createCar(CreateCarRequest request);
}
