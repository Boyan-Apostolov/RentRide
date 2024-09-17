package nl.fontys.s3.rentride_be.business.impl.city;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.useCases.city.DeleteCityUseCase;
import nl.fontys.s3.rentride_be.persistance.CityRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteCityUseCaseImpl implements DeleteCityUseCase {
    private CityRepository cityRepository;

    @Override
    public void deleteCity(Long cityId) {
        this.cityRepository.deleteById(cityId);
    }
}
