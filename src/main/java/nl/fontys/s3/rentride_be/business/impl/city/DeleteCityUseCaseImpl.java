package nl.fontys.s3.rentride_be.business.impl.city;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.use_cases.city.DeleteCityUseCase;
import nl.fontys.s3.rentride_be.persistance.CityRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteCityUseCaseImpl implements DeleteCityUseCase {
    private CityRepository cityRepository;

    @Override
    public void deleteCity(Long cityId) {
        if (!cityRepository.existsById(cityId)) {
            throw new NotFoundException("Delete->City");
        }
        this.cityRepository.deleteById(cityId);
    }
}
