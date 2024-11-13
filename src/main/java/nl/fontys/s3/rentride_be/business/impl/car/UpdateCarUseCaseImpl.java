package nl.fontys.s3.rentride_be.business.impl.car;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.use_cases.car.UpdateCarUseCase;
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
import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateCarUseCaseImpl implements UpdateCarUseCase {
    private CarRepository carRepository;
    private CityRepository cityRepository;
    private CarFeatureRepository carFeatureRepository;

    @Override
    public void updateCar(UpdateCarRequest request) {
        prepareCity(request);
        prepareCarFeatures(request);

        updateEntity(request);
    }

    private void prepareCity(UpdateCarRequest request) {
        Optional<CityEntity> cityOptional = this.cityRepository.findById(request.getCityId());
        if (cityOptional.isEmpty()) {
            throw new NotFoundException("Update->Car->City");
        }
        request.setFoundCity(cityOptional.get());
    }

    private void prepareCarFeatures(UpdateCarRequest request) {
        if(request.getFeatures() == null)return;

        List<CarFeatureEntity> foundFeatures = new ArrayList<>();
        for(int i = 0; i < request.getFeatures().size(); i++) {
            String currentRequestFeature = request.getFeatures().get(i);
            CarFeatureType featureType = CarFeatureType.class.getEnumConstants()[i];

            CarFeatureEntity foundFeature = this.carFeatureRepository.findByFeatureTextAndFeatureType(currentRequestFeature, featureType);

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
        Optional<CarEntity> carOptional = this.carRepository.findById(request.getId());
        if (carOptional.isEmpty()) {
            throw new NotFoundException("Update->Car");
        }
        CarEntity carEntity = carOptional.get();

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
