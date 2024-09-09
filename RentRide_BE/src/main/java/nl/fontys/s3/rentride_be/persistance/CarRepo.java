package nl.fontys.s3.rentride_be.persistance;

import nl.fontys.s3.rentride_be.domain.Car;

import java.util.List;

public interface CarRepo {
    List<Car> getCars();
    void addCar(Car car);
}
