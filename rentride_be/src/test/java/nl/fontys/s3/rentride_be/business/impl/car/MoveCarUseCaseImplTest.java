package nl.fontys.s3.rentride_be.business.impl.car;

import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.persistance.CarRepository;
import nl.fontys.s3.rentride_be.persistance.CityRepository;
import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MoveCarUseCaseImplTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private MoveCarUseCaseImpl moveCarUseCase;

    private CarEntity carEntity;
    private CityEntity cityEntity;

    @BeforeEach
    void setUp() {
        carEntity = CarEntity.builder()
                .id(1L)
                .make("Test Make")
                .model("Test Model")
                .build();

        cityEntity = CityEntity.builder()
                .id(2L)
                .name("Test City")
                .build();
    }

    @Test
    void moveCar_ShouldMoveCarToCity_WhenCarAndCityExist() {
        Long carId = 1L;
        Long cityId = 2L;

        when(carRepository.findById(carId)).thenReturn(Optional.of(carEntity));
        when(cityRepository.findById(cityId)).thenReturn(Optional.of(cityEntity));

        moveCarUseCase.moveCar(carId, cityId);

        verify(carRepository, times(1)).findById(carId);
        verify(cityRepository, times(1)).findById(cityId);

        assertEquals(cityEntity, carEntity.getCity());

        verify(carRepository, times(1)).save(carEntity);
    }

    @Test
    void moveCar_ShouldThrowNotFoundException_WhenCarDoesNotExist() {
        Long carId = 1L;
        Long cityId = 2L;

        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> moveCarUseCase.moveCar(carId, cityId));

        verify(carRepository, times(1)).findById(carId);
        verify(cityRepository, never()).findById(any());
        verify(carRepository, never()).save(any());
    }

    @Test
    void moveCar_ShouldThrowNotFoundException_WhenCityDoesNotExist() {
        Long carId = 1L;
        Long cityId = 2L;

        when(carRepository.findById(carId)).thenReturn(Optional.of(carEntity));
        when(cityRepository.findById(cityId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> moveCarUseCase.moveCar(carId, cityId));

        verify(carRepository, times(1)).findById(carId);
        verify(cityRepository, times(1)).findById(cityId);
        verify(carRepository, never()).save(any());
    }
}