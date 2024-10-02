package nl.fontys.s3.rentride_be.business.impl.car;

import nl.fontys.s3.rentride_be.domain.car.Car;
import nl.fontys.s3.rentride_be.persistance.CarRepository;
import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCarUseCaseImplTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private GetCarUseCaseImpl getCarUseCase;;

    @Test
    void getCar_ShouldReturnNullWhenCityDoesNotExist() {
        Car foundCar = this.getCarUseCase.getCar(1L);

        assertNull(foundCar);

        verify(this.carRepository).findById(1L);
    }

    @Test
    void getCar_ShouldReturnCarWithCorrectIdExists() {
        CarEntity car =  CarEntity.builder()
                .id(1L)
                .make("Ford")
                .model("Fiesta")
                .registrationNumber("BT2142KX")
                .seatsCount(5)
                .fuelConsumption(5.5)
                .transmissionType(CarTransmissionType.Manual)
                .build();

        when(this.carRepository.findById(1L)).thenReturn(car);

        Car foundCar = this.getCarUseCase.getCar(1L);

        assertEquals(CarConverter.convert(car), foundCar);

        verify(this.carRepository).findById(1L);
    }

}