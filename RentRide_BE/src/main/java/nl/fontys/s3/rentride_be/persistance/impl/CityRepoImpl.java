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
public class CityRepoImpl implements CityRepo {
    private List<City> cities;

    public CityRepoImpl() {
        this.cities = new ArrayList<>();
        citySeeder();
    }

    @Override
    public List<City> getCities() {
        return Collections.unmodifiableList(this.cities);
    }

    @Override
    public void addCity(City city) {
        this.cities.add(city);
    }

    private void citySeeder(){
        this.cities.add(new City("Eindhoven", 52.23323, 5.124124));
        this.cities.add(new City("Amsterdam", 53.23323, 6.124124));
        this.cities.add(new City("Breda", 54.23323, 7.124124));
    }
}
