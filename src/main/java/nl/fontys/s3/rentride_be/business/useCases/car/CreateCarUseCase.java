package nl.fontys.s3.rentride_be.business.useCases.car;

import nl.fontys.s3.rentride_be.domain.car.CreateCarRequest;
import nl.fontys.s3.rentride_be.domain.car.CreateCarResponse;
import nl.fontys.s3.rentride_be.domain.city.CreateCityRequest;
import nl.fontys.s3.rentride_be.domain.city.CreateCityResponse;

public interface CreateCarUseCase {
    CreateCarResponse createCar(CreateCarRequest request);
}
