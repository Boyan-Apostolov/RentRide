package nl.fontys.s3.rentride_be.persistance.impl;

import nl.fontys.s3.rentride_be.domain.Car;
import nl.fontys.s3.rentride_be.domain.CarTransmissionType;
import nl.fontys.s3.rentride_be.domain.City;
import nl.fontys.s3.rentride_be.persistance.CarRepo;
import nl.fontys.s3.rentride_be.persistance.CityRepo;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class CarRepoImpl implements CarRepo {
    private final CityRepo cityRepo;
    private List<Car> cars;

    public CarRepoImpl(CityRepo cityRepo) {
        this.cityRepo = cityRepo;
        this.cars = new ArrayList<>();
    }

    @Override
    public List<Car> getCars() {
        return Collections.unmodifiableList(this.cars);
    }

    @Override
    public void addCar(Car car) {
        this.cars.add(car);
    }

    @EventListener(ApplicationReadyEvent.class)
    private void carSeeder(){
        List<City> cities = cityRepo.getCities();

        this.cars.add(new Car("Form", "Fiesta", "BT2142KX", 5, CarTransmissionType.Manual, 5.5, cities.get(0)));
        this.cars.add(new Car("BMW", "E46", "CB2125KA", 3, CarTransmissionType.Automatic, 7.0, cities.get(1)));
        this.cars.add(new Car("Volkswagen", "Tuaran", "CB1234MK", 5, CarTransmissionType.Manual, 4.5, cities.get(2)));
    }
}
