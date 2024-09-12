package nl.fontys.s3.rentride_be.business.useCases.city;

import nl.fontys.s3.rentride_be.domain.city.CreateCityRequest;
import nl.fontys.s3.rentride_be.domain.city.CreateCityResponse;

public interface CreateCityUseCase {
    CreateCityResponse createCity(CreateCityRequest request);
}
