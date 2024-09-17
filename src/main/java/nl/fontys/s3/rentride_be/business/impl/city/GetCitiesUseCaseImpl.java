package nl.fontys.s3.rentride_be.business.impl.city;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.useCases.city.GetCitiesUseCase;
import nl.fontys.s3.rentride_be.domain.city.City;
import nl.fontys.s3.rentride_be.persistance.CityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetCitiesUseCaseImpl implements GetCitiesUseCase {
    private CityRepository cityRepository;

    @Override
    public List<City> getCities() {
        return cityRepository
                .findAll()
                .stream()
                .map(CityConverter::convert)
                .toList();
    }
}
