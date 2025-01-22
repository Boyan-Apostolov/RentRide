package nl.fontys.s3.rentride_be.business.impl.car;

import nl.fontys.s3.rentride_be.domain.car.Car;
import nl.fontys.s3.rentride_be.persistance.CarRepository;
import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetExclusiveCarsUseCaseImplTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private GetExclusiveCarsUseCaseImpl getExclusiveCarsUseCase;

    @Test
    void getCars_shouldReturnOnlyExclusiveCars() {
        CarEntity exclusiveCar1 = CarEntity.builder().id(1L).make("Brand1").model("Model1").features(List.of()).isExclusive(true).build();
        CarEntity exclusiveCar2 = CarEntity.builder().id(2L).make("Brand2").model("Model2").features(List.of()).isExclusive(true).build();
        CarEntity regularCar = CarEntity.builder().id(3L).make("Brand3").model("Model3").features(List.of()).isExclusive(false).build();

        when(carRepository.findAll()).thenReturn(List.of(exclusiveCar1, exclusiveCar2, regularCar));

        List<Car> result = getExclusiveCarsUseCase.getCars();

        assertThat(result).hasSize(2);
        assertThat(result).extracting("id").containsExactly(1L, 2L);

        verify(carRepository).findAll();
    }

    @Test
    void getCars_shouldReturnEmptyList_whenNoExclusiveCars() {
        CarEntity regularCar1 = CarEntity.builder().id(1L).make("Brand1").model("Model1").features(List.of()).isExclusive(false).build();
        CarEntity regularCar2 = CarEntity.builder().id(2L).make("Brand2").model("Model2").features(List.of()).isExclusive(false).build();

        when(carRepository.findAll()).thenReturn(List.of(regularCar1, regularCar2));

        List<Car> result = getExclusiveCarsUseCase.getCars();

        assertThat(result).isEmpty();

        verify(carRepository).findAll();
    }
}