package nl.fontys.s3.rentride_be.business.impl.car;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.useCases.car.GetAvailableCarsUseCase;
import nl.fontys.s3.rentride_be.business.useCases.city.GetRouteBetweenCitiesUseCase;
import nl.fontys.s3.rentride_be.domain.car.Car;
import nl.fontys.s3.rentride_be.domain.car.GetAvailableCarsRequest;
import nl.fontys.s3.rentride_be.domain.city.GetRouteResponse;
import nl.fontys.s3.rentride_be.persistance.CarRepository;
import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class GetAvailableCarsUseCaseImpl implements GetAvailableCarsUseCase {
    private CarRepository carRepository;
    private GetRouteBetweenCitiesUseCase getRouteBetweenCitiesUseCase;

    @Transactional
    @Override
    public List<Car> getAvailableCars(GetAvailableCarsRequest request) {
        //TODO: Filter availability when implemented
        GetRouteResponse routeData = getRouteBetweenCitiesUseCase.getRoute(Long.parseLong(request.getFromCity()), Long.parseLong(request.getToCity()));
        Double distance = Double.parseDouble(routeData.getDistance());

        List<Car> availableCars = new ArrayList<>();
        List<String> selectedFeatures = request.getSelectedFeatures();

        if (selectedFeatures != null && !selectedFeatures.isEmpty()) {
            availableCars = this.carRepository
                    .findAll()
                    .stream()
                    .filter(carEntity ->
                            selectedFeatures.stream().allMatch(sf ->
                                    carEntity.getFeatures().stream()
                                            .anyMatch(cf -> cf.getId() == Integer.parseInt(sf)) // Correctly compare IDs
                                            || carEntity.getMake().contains(sf) // Also check if the car's make contains the feature string (if relevant)
                            )
                    )
                    .map(CarConverter::convert)
                    .toList();
        } else {
            availableCars = this.carRepository.findAll().stream()
                    .map(CarConverter::convert)
                    .toList();
        }
        availableCars.forEach(c -> c.setFuelPrice((distance / 100) * c.getFuelConsumption()));
        return availableCars;
    }
}
