package nl.fontys.s3.rentride_be.business.impl.city;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.AlreadyExistsException;
import nl.fontys.s3.rentride_be.business.use_cases.city.CreateCityUseCase;
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
            throw new AlreadyExistsException("City");
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
                .depoAdress(request.getDepoAddress())
                .build();

        return this.cityRepository.save(cityEntity);
    }
}
