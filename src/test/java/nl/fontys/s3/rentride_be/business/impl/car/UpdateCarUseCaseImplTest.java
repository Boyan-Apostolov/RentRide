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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
        when(this.carRepository.findById(1L))
                .thenThrow(new NotFoundException("Car"));

        assertThrows(NotFoundException.class,
                () -> this.updateCarUseCase.updateCar(
                        UpdateCarRequest.builder()
                                .id(1L)
                                .make("Ford")
                                .model("Fiesta")
                                .registrationNumber("BT2142KX")
                                .seatsCount(5)
                                .fuelConsumption(5.5)
                                .transmissionType(0)
                                .build()
                ));

        verify(this.carRepository).findById(1L);
    }

    @Test
    void updateCar_withValidIdShouldUpdateTheEntity() {
        CityEntity cityEntity = CityEntity.builder()
                .name("Eindhoven")
                .lon(55.5)
                .lat(53.3)
                .build();
        when(this.cityRepository.findById(1L)).thenReturn(cityEntity);

        Long carId = 1L;

        CarEntity existingCar = CarEntity.builder()
                .id(carId)
                .make("Ford")
                .model("Fiesta")
                .registrationNumber("BT2142KX")
                .fuelConsumption(5.5)
                .city(this.cityRepository.findById(1L))
                .build();

        CarEntity updatedCar = CarEntity.builder()
                .id(carId)
                .make("Ford-edit")
                .model("Fiestaa!")
                .registrationNumber("BT2142KX")
                .fuelConsumption(5.5)
                .city(this.cityRepository.findById(1L))
                .build();

        when(this.carRepository.findById(carId)).thenReturn(existingCar);
        when(this.carRepository.save(any(CarEntity.class))).thenReturn(updatedCar);

        this.updateCarUseCase.updateCar(
                UpdateCarRequest.builder()
                        .id(carId)
                        .make("Ford-edit")
                        .model("Fiestaa!")
                        .registrationNumber("BT2142KX")
                        .seatsCount(5)
                        .fuelConsumption(5.5)
                        .transmissionType(0)
                        .cityId(1L)
                        .build()
        );

        assertEquals("Ford-edit", existingCar.getMake());
        assertEquals("Fiestaa!", existingCar.getModel());

        verify(this.carRepository).save(existingCar);
        verify(this.carRepository, times(2)).findById(carId);
    }
}