package nl.fontys.s3.rentride_be.business.impl.car;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.useCases.car.GetAvailableCarsUseCase;
import nl.fontys.s3.rentride_be.domain.car.Car;
import nl.fontys.s3.rentride_be.domain.car.GetAvailableCarsRequest;
import nl.fontys.s3.rentride_be.persistance.CarRepository;
import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class GetAvailableCarsUseCaseImpl implements GetAvailableCarsUseCase {
    private CarRepository carRepository;

    @Override
    public List<Car> getAvailableCars(GetAvailableCarsRequest request) {
        //TODO: Filter availability when implemented

        List<String> selectedFeatures = request.getSelectedFeatures();
        if (selectedFeatures != null && !selectedFeatures.isEmpty()) {
            return this.carRepository
                    .findAll()
                    .stream()
                    .filter(carEntity ->
                            // Ensure all selected features are present in the car's features
                            selectedFeatures.stream().allMatch(sf ->
                                    carEntity.getFeatures().stream()
                                            .anyMatch(cf -> cf.getFeatureText().contains(sf))
                                    || carEntity.getMake().contains(sf)
                            )
                    )
                    .map(CarConverter::convert)
                    .toList();
        }
        return this.carRepository.findAll().stream().map(CarConverter::convert).toList();
    }
}
