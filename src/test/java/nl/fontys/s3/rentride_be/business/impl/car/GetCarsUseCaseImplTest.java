package nl.fontys.s3.rentride_be.business.impl.car;

import nl.fontys.s3.rentride_be.domain.car.Car;
import nl.fontys.s3.rentride_be.persistance.CarRepository;
import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCarsUseCaseImplTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private GetCarsUseCaseImpl getCarsUseCase;

    @Test
    void getAllCarsShouldReturnAllCars() {
        List<CarEntity> cars = new ArrayList<>();
        cars.add(
                CarEntity.builder()
                        .make("Ford")
                        .model("Fiesta")
                        .registrationNumber("BT2142KX")
                        .seatsCount(5)
                        .fuelConsumption(5.5)
                        .transmissionType(CarTransmissionType.Manual)
                        .build()
        );
        cars.add(
                CarEntity.builder()
                        .make("VW")
                        .model("Tuaran")
                        .registrationNumber("BT7282KR")
                        .seatsCount(5)
                        .fuelConsumption(5.5)
                        .transmissionType(CarTransmissionType.Automatic)
                        .build()
        );

        when(this.carRepository.findAll()).thenReturn(cars);

        List<Car> actualCars = this.getCarsUseCase.getCars();
        List<Car> expectedCars = cars.stream().map(CarConverter::convert).toList();

        assertEquals(expectedCars, actualCars);
        verify(this.carRepository).findAll();
    }
}