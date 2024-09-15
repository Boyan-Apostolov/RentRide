package nl.fontys.s3.rentride_be.business.impl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.CityAlreadyExistsException;
import nl.fontys.s3.rentride_be.business.useCases.city.CreateCityUseCase;
import nl.fontys.s3.rentride_be.domain.city.CreateCityRequest;
import nl.fontys.s3.rentride_be.domain.city.CreateCityResponse;
import nl.fontys.s3.rentride_be.persistance.CityRepository;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateCityUseCaseImpl implements CreateCityUseCase {
    private CityRepository cityRepository;

    @Override
    public CreateCityResponse createCity(CreateCityRequest request) {
        if(cityRepository.existsByName(request.getName())) {
            throw new CityAlreadyExistsException();
        }

        CityEntity savedCity = saveNewCity(request);

        return CreateCityResponse.builder()
                .cityId(savedCity.getId())
                .build();
    }

    private CityEntity saveNewCity(CreateCityRequest request) {
        CityEntity cityEntity = CityEntity.builder()
                .name(request.getName())
                .lat(request.getLat())
                .lon(request.getLon())
                .build();

        return this.cityRepository.save(cityEntity);
    }
}
