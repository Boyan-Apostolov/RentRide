package nl.fontys.s3.rentride_be.persistance;

import nl.fontys.s3.rentride_be.domain.City;

import java.util.List;

public interface CityRepo {
    List<City> getCities();
    void addCity(City city);
}
