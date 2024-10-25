package nl.fontys.s3.rentride_be.business.use_cases.city;

import nl.fontys.s3.rentride_be.domain.city.City;

import java.util.List;

public interface GetCitiesUseCase {
    List<City> getCities();
}
