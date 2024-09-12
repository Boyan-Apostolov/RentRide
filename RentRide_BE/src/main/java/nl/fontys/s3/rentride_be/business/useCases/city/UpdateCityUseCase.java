package nl.fontys.s3.rentride_be.business.useCases.city;

import nl.fontys.s3.rentride_be.domain.city.UpdateCityRequest;

public interface UpdateCityUseCase {
    void updateCity(UpdateCityRequest request);
}
