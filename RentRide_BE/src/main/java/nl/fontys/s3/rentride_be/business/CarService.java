package nl.fontys.s3.rentride_be.business;

import nl.fontys.s3.rentride_be.domain.Car;

import java.util.List;

public interface CarService {
    List<Car> getCars();
    void addCar(Car car);
}
