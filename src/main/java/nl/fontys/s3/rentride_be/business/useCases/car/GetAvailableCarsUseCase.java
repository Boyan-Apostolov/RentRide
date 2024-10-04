package nl.fontys.s3.rentride_be.business.useCases.car;

import nl.fontys.s3.rentride_be.domain.car.Car;
import nl.fontys.s3.rentride_be.domain.car.GetAvailableCarsRequest;

import java.util.List;

public interface GetAvailableCarsUseCase {
    public List<Car> getAvailableCars(GetAvailableCarsRequest request);
}
