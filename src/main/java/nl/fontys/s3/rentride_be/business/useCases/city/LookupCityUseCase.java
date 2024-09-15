package nl.fontys.s3.rentride_be.business.useCases.city;

import nl.fontys.s3.rentride_be.domain.city.GeoapifyResult;

public interface LookupCityUseCase {
    GeoapifyResult lookupCity(String cityName);
}
