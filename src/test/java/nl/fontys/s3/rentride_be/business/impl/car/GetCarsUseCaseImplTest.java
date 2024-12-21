package nl.fontys.s3.rentride_be.business.impl.car;

import nl.fontys.s3.rentride_be.domain.car.Car;
import nl.fontys.s3.rentride_be.persistance.CarRepository;
import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCarsUseCaseImplTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private GetCarsUseCaseImpl getCarsUseCase;

    private CarEntity carEntity1;
    private CarEntity carEntity2;
    private int defaultPageSize = 10;

    @BeforeEach
    void setUp() {
        carEntity1 = CarEntity.builder()
                .id(1L)
                .make("Tesla Model S")
                .isExclusive(false)
                .features(List.of())
                .build();

        carEntity2 = CarEntity.builder()
                .id(2L)
                .make("Porsche 911")
                .isExclusive(true)
                .features(List.of())
                .build();

        // Inject the default page size using reflection (simulate @Value injection)
        ReflectionTestUtils.setField(getCarsUseCase, "DefaultPageSize", defaultPageSize);
    }

    @Test
    void getCars_ShouldReturnAllCars() {
        when(carRepository.findAll()).thenReturn(List.of(carEntity1, carEntity2));

        List<Car> result = getCarsUseCase.getCars();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getMake()).isEqualTo("Tesla Model S");
        assertThat(result.get(1).getMake()).isEqualTo("Porsche 911");

        verify(carRepository, times(1)).findAll();
    }

    @Test
    void getCars_WithPagination_ShouldReturnPagedCars() {
        Page<CarEntity> pagedCars = new PageImpl<>(List.of(carEntity1));
        when(carRepository.findAll(PageRequest.of(0, defaultPageSize))).thenReturn(pagedCars);

        List<Car> result = getCarsUseCase.getCars(0);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMake()).isEqualTo("Tesla Model S");

        verify(carRepository, times(1)).findAll(PageRequest.of(0, defaultPageSize));
    }

    @Test
    void getCount_ShouldReturnTotalCarCount() {
        when(carRepository.count()).thenReturn(2L);

        Long count = getCarsUseCase.getCount();

        assertThat(count).isEqualTo(2);
        verify(carRepository, times(1)).count();
    }
}