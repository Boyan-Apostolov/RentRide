package nl.fontys.s3.rentride_be.persistance;

import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;

import java.util.List;

public interface CarRepository {
    boolean existsByRegistrationNumber(String number);

    boolean existsById(long carId);

    CarEntity findById(long carId);

    void deleteById(long carId);

    CarEntity save(CarEntity car);

    List<CarEntity> findAll();

    int count();
}
