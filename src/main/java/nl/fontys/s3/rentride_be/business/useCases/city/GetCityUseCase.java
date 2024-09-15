package nl.fontys.s3.rentride_be.business.useCases.city;

import nl.fontys.s3.rentride_be.domain.city.City;

public interface GetCityUseCase {
    City getCity(Long cityId);
}
