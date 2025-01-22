package nl.fontys.s3.rentride_be.business.impl.car;

import nl.fontys.s3.rentride_be.business.use_cases.city.GetRouteBetweenCitiesUseCase;
import nl.fontys.s3.rentride_be.domain.car.Car;
import nl.fontys.s3.rentride_be.domain.car.GetAvailableCarsRequest;
import nl.fontys.s3.rentride_be.domain.city.GetRouteResponse;
import nl.fontys.s3.rentride_be.persistance.CarRepository;
import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
import nl.fontys.s3.rentride_be.persistance.entity.CarFeatureEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAvailableCarsUseCaseImplTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private GetRouteBetweenCitiesUseCase getRouteBetweenCitiesUseCase;

    @InjectMocks
    private GetAvailableCarsUseCaseImpl getAvailableCarsUseCase;

    private GetAvailableCarsRequest request;
    private GetRouteResponse routeResponse;
    private CarEntity carWithFeatures;
    private CarEntity carWithoutFeatures;
    private CarEntity carMatchingMake;

    @BeforeEach
    void setUp() {
        request = new GetAvailableCarsRequest();
        request.setFromCity("1");
        request.setToCity("2");
        request.setSelectedFeatures(List.of("1", "2")); // Filter by feature IDs

        routeResponse = new GetRouteResponse();
        routeResponse.setDistance("100"); // Set distance in km
        routeResponse.setTime("60");

        carWithFeatures = new CarEntity();
        carWithFeatures.setId(1L);
        carWithFeatures.setMake("Toyota");
        carWithFeatures.setFuelConsumption(5.0); // 5L per 100km

        CarFeatureEntity feature1 = new CarFeatureEntity();
        feature1.setId(1L);
        CarFeatureEntity feature2 = new CarFeatureEntity();
        feature2.setId(2L);

        // Initialize with features
        carWithFeatures.setFeatures(List.of(feature1, feature2));

        carWithoutFeatures = new CarEntity();
        carWithoutFeatures.setId(2L);
        carWithoutFeatures.setMake("Honda");
        carWithoutFeatures.setFuelConsumption(6.0); // 6L per 100km

        carWithoutFeatures.setFeatures(List.of());

        carMatchingMake = new CarEntity();
        carMatchingMake.setId(3L);
        carMatchingMake.setMake("Speedster 2"); // Make contains the feature string "2"
        carMatchingMake.setFuelConsumption(4.5);
        carMatchingMake.setFeatures(List.of()); // No features, matches only by make
    }

    @Test
    void getAvailableCars_ShouldReturnEmptyList_WhenNoCarsMatchSelectedFeatures() {
        // Test when no car has the required features or make
        request.setSelectedFeatures(List.of("99")); // No car has feature ID 99
        when(getRouteBetweenCitiesUseCase.getRoute(1L, 2L)).thenReturn(routeResponse);
        when(carRepository.findAll()).thenReturn(List.of(carWithFeatures, carWithoutFeatures, carMatchingMake));

        List<Car> result = getAvailableCarsUseCase.getAvailableCars(request);

        assertEquals(0, result.size());
    }

    @Test
    void getAvailableCars_ShouldIncludeCar_WhenMakeMatchesSelectedFeature() {
        request.setSelectedFeatures(List.of("2")); // Feature "2" should match carMatchingMake by its make
        when(getRouteBetweenCitiesUseCase.getRoute(1L, 2L)).thenReturn(routeResponse);
        when(carRepository.findAll()).thenReturn(List.of(carWithFeatures, carWithoutFeatures, carMatchingMake));

        List<Car> result = getAvailableCarsUseCase.getAvailableCars(request);

        assertEquals(2, result.size()); // carWithFeatures and carMatchingMake should match
        assertEquals(carWithFeatures.getId(), result.get(0).getId());
        assertEquals(carMatchingMake.getId(), result.get(1).getId());
    }

    @Test
    void getAvailableCars_ShouldCalculateFuelPriceCorrectly() {
        when(getRouteBetweenCitiesUseCase.getRoute(1L, 2L)).thenReturn(routeResponse);
        when(carRepository.findAll()).thenReturn(List.of(carWithFeatures));

        List<Car> result = getAvailableCarsUseCase.getAvailableCars(request);

        double expectedFuelPrice = (100.0 / 100) * carWithFeatures.getFuelConsumption(); // distance/100 * fuelConsumption
        assertEquals(expectedFuelPrice, result.get(0).getFuelPrice());
        verify(getRouteBetweenCitiesUseCase, times(1)).getRoute(1L, 2L);
    }
}