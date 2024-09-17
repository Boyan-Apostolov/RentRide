package nl.fontys.s3.rentride_be.business.useCases.car;

import nl.fontys.s3.rentride_be.domain.car.UpdateCarRequest;
import nl.fontys.s3.rentride_be.domain.city.UpdateCityRequest;

public interface UpdateCarUseCase {
    void updateCar(UpdateCarRequest request);
}
