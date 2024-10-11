package nl.fontys.s3.rentride_be.business.impl.car;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.useCases.car.UpdateCarUseCase;
import nl.fontys.s3.rentride_be.domain.car.CarFeatureType;
import nl.fontys.s3.rentride_be.domain.car.UpdateCarRequest;
import nl.fontys.s3.rentride_be.persistance.CarFeatureRepository;
import nl.fontys.s3.rentride_be.persistance.CarRepository;
import nl.fontys.s3.rentride_be.persistance.CityRepository;
import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
import nl.fontys.s3.rentride_be.persistance.entity.CarFeatureEntity;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UpdateCarUseCaseImpl implements UpdateCarUseCase {
    private CarRepository carRepository;
    private CityRepository cityRepository;
    private CarFeatureRepository carFeatureRepository;

    @Override
    public void updateCar(UpdateCarRequest request) {
        verifyCarExists(request.getId());

        prepareCity(request);
        prepareCarFeatures(request);

        updateEntity(request);
    }

    private void prepareCity(UpdateCarRequest request) {
        CityEntity cityOptional = this.cityRepository.findById(request.getCityId());
        if (cityOptional == null) {
            throw new NotFoundException("Update->Car->City");
        }
        request.setFoundCity(cityOptional);
    }

    private void verifyCarExists(Long carId){
        CarEntity carOptional = this.carRepository.findById(carId);
        if (carOptional == null) {
            throw new NotFoundException("Update->Car");
        }
    }

    private void prepareCarFeatures(UpdateCarRequest request) {
        if(request.getFeatures() == null)return;

        List<CarFeatureEntity> foundFeatures = new ArrayList<>();
        for(int i = 0; i < request.getFeatures().size(); i++) {
            String currentRequestFeature = request.getFeatures().get(i);
            CarFeatureType featureType = CarFeatureType.class.getEnumConstants()[i];

            CarFeatureEntity foundFeature = this.carFeatureRepository.findByFeatureTextAndType(currentRequestFeature, featureType);

            if(foundFeature == null){
                foundFeature = this.carFeatureRepository.save(CarFeatureEntity.builder()
                        .featureText(currentRequestFeature)
                        .featureType(featureType)
                        .build());
            }

            foundFeatures.add(foundFeature);
        }
        request.setFoundFeatures(foundFeatures);
    }

    private void updateEntity(UpdateCarRequest request) {
        CarEntity carEntity = this.carRepository.findById(request.getId());

        carEntity.setMake(request.getMake());
        carEntity.setModel(request.getModel());
        carEntity.setRegistrationNumber(request.getRegistrationNumber());
        carEntity.setFuelConsumption(request.getFuelConsumption());
        carEntity.setCity(request.getFoundCity());
        carEntity.setPhotosBase64(request.getPhotosBase64());
        carEntity.setFeatures(request.getFoundFeatures());

        this.carRepository.save(carEntity);
    }
}
