package nl.fontys.s3.rentride_be.business.impl.car;

import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.domain.car.UpdateCarRequest;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCarUseCaseImplTest {
    @Mock
    private CarRepository carRepository;

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private UpdateCarUseCaseImpl updateCarUseCase;

    @Test
    void updateCar_withInvalidIdShouldThrowException() {
        UpdateCarRequest request = UpdateCarRequest.builder()
                .id(1L)
                .make("Ford")
                .model("Fiesta")
                .registrationNumber("BT2142KX")
                .fuelConsumption(5.5)
                .foundFeatures(List.of())
                .build();

        assertThrows(NotFoundException.class, () -> this.updateCarUseCase.updateCar(request));
    }

    @Test
    void updateCar_withValidIdShouldUpdateTheEntity() {
        Optional<CityEntity> cityEntity = Optional.of(CityEntity.builder()
                .name("Eindhoven")
                .lon(55.5)
                .lat(53.3)
                .build());

        when(this.cityRepository.findById(1L)).thenReturn(cityEntity);

        Long carId = 1L;

        Optional<CarEntity> existingCar = Optional.of( CarEntity.builder()
                .id(carId)
                .make("Ford")
                .model("Fiesta")
                .registrationNumber("BT2142KX")
                .fuelConsumption(5.5)
                .features(List.of())
                .city(this.cityRepository.findById(1L).get())
                .build());

        CarEntity updatedCar = CarEntity.builder()
                .id(carId)
                .make("Ford-edit")
                .model("Fiestaa!")
                .registrationNumber("BT2142KX")
                .fuelConsumption(5.5)
                .features(List.of())

                .city(this.cityRepository.findById(1L).get())
                .build();

        when(this.carRepository.findById(carId)).thenReturn(existingCar);
        when(this.carRepository.save(any(CarEntity.class))).thenReturn(updatedCar);

        this.updateCarUseCase.updateCar(
                UpdateCarRequest.builder()
                        .id(carId)
                        .make("Ford-edit")
                        .model("Fiestaa!")
                        .registrationNumber("BT2142KX")
                        .fuelConsumption(5.5)
                        .foundFeatures(List.of())
                        .cityId(1L)
                        .build()
        );

        assertEquals("Ford-edit", existingCar.get().getMake());
        assertEquals("Fiestaa!", existingCar.get().getModel());

        verify(this.carRepository).save(existingCar.get());
        verify(this.carRepository, times(1)).findById(carId);
    }
}