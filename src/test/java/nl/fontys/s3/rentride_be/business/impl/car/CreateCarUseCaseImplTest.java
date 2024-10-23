package nl.fontys.s3.rentride_be.business.impl.car;

import nl.fontys.s3.rentride_be.business.exception.AlreadyExistsException;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.domain.car.CreateCarRequest;
import nl.fontys.s3.rentride_be.domain.car.CreateCarResponse;
import nl.fontys.s3.rentride_be.persistance.CarRepository;
import nl.fontys.s3.rentride_be.persistance.CityRepository;
import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCarUseCaseImplTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CreateCarUseCaseImpl createCarUseCase;

    @Test
     void createCar_withDuplicatedNumber_shouldThrowException() {
        String duplicatedRegNumber = "BT2142KX";

        when(this.carRepository.existsByRegistrationNumber(duplicatedRegNumber))
                .thenThrow(new AlreadyExistsException("Car"));

        CreateCarRequest createCarRequest = CreateCarRequest.builder()
                .make("Ford")
                .model("Fiesta")
                .registrationNumber(duplicatedRegNumber)
                .build();

        assertThrows(AlreadyExistsException.class, () ->
                this.createCarUseCase.createCar(createCarRequest)
        );

        verify(this.carRepository).existsByRegistrationNumber(duplicatedRegNumber);
    }

    @Test
     void createCar_withNonExistingCity_shouldThrowException() {
        CreateCarRequest createCarRequest = CreateCarRequest.builder()
                .make("Ford")
                .model("Fiesta")
                .registrationNumber("BT2142KX")
                .cityId(1L)
                .build();

        assertThrows(NotFoundException.class, () ->
                this.createCarUseCase.createCar(createCarRequest)
        );

        verify(this.cityRepository).findById(1L);
    }

    @Test
     void createCar_shouldCorrectlyAddTheCityWithValidRegNumber() {
        CarEntity carEntity = CarEntity.builder()
                .make("Ford")
                .model("Fiesta")
                .registrationNumber("BT2142KX")
                .fuelConsumption(5.5)
                .build();
        when(this.carRepository.save(any(CarEntity.class))).thenReturn(carEntity);

        Optional<CityEntity> cityEntity = Optional.of( CityEntity.builder()
                .name("Eindhoven")
                .lon(55.5)
                .lat(53.3)
                .build());
        when(this.cityRepository.findById(1L)).thenReturn(cityEntity);

        CreateCarResponse createdCar = this.createCarUseCase.createCar(
                CreateCarRequest.builder()
                        .make("Ford")
                        .model("Fiesta")
                        .registrationNumber("BT2142KX")
                        .fuelConsumption(5.5)
                        .cityId(1L)
                        .features(List.of())
                        .build()
        );

        assertEquals(carEntity.getId(), createdCar.getCarId());

        verify(this.carRepository).save(any(CarEntity.class));
    }
}