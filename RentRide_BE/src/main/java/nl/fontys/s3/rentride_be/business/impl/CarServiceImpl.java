package nl.fontys.s3.rentride_be.business.impl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.CarService;
import nl.fontys.s3.rentride_be.domain.Car;
import nl.fontys.s3.rentride_be.persistance.CarRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CarServiceImpl implements CarService {
    private CarRepo carRepo;

    @Override
    public List<Car> getCars() {
        return this.carRepo.getCars();
    }

    @Override
    public void addCar(Car car) {
        this.carRepo.addCar(car);
    }
}
