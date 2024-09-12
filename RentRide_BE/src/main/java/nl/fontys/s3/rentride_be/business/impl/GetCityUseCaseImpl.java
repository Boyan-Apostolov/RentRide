package nl.fontys.s3.rentride_be.business.impl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.useCases.city.GetCityUseCase;
import nl.fontys.s3.rentride_be.domain.city.City;
import nl.fontys.s3.rentride_be.persistance.CityRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GetCityUseCaseImpl implements GetCityUseCase {
    private CityRepository cityRepository;

    @Override
    public City getCity(Long cityId) {
        return CityConverter.convert(this.cityRepository.findById(cityId));
    }
}
