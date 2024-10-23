package nl.fontys.s3.rentride_be.business.impl.city;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.useCases.city.UpdateCityUseCase;
import nl.fontys.s3.rentride_be.domain.city.UpdateCityRequest;
import nl.fontys.s3.rentride_be.persistance.CityRepository;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateCityUseCaseImpl implements UpdateCityUseCase {
    private CityRepository cityRepository;

    @Override
    public void updateCity(UpdateCityRequest request) {
        verifyObjectExists(request.getId());
        updateEntity(request);
    }

    private void verifyObjectExists(Long cityId){
        Optional<CityEntity> cityOptional = this.cityRepository.findById(cityId);
        if (cityOptional.isEmpty()) {
            throw new NotFoundException("CITY");
        }
    }

    private void updateEntity(UpdateCityRequest request) {
        Optional<CityEntity> foundCity = this.cityRepository.findById(request.getId());
        if(foundCity.isEmpty()) return;
        CityEntity cityEntity = foundCity.get();

        cityEntity.setName(request.getName());
        cityEntity.setLat(request.getLat());
        cityEntity.setLon(request.getLon());
        cityEntity.setDepoAdress(request.getDepoAddress());

        this.cityRepository.save(cityEntity);
    }
}
