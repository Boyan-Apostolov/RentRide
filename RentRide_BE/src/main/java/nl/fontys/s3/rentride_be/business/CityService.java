package nl.fontys.s3.rentride_be.business;

import nl.fontys.s3.rentride_be.domain.City;

import java.util.List;

public interface CityService {
    List<City> getCities();
    void addCity(City city);
}
