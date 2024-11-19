package nl.fontys.s3.rentride_be.business.impl.complex_queries;

import nl.fontys.s3.rentride_be.domain.statistics.GroupingDto;
import nl.fontys.s3.rentride_be.persistance.BookingRepository;
import nl.fontys.s3.rentride_be.persistance.entity.BookingEntity;
import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComplexBookingRepositoryQueriesUseCaseImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private ComplexBookingRepositoryQueriesUseCaseImpl complexQueries;

    private List<BookingEntity> mockBookings;

    @BeforeEach
    void setUp() {
        CarEntity car1 = CarEntity.builder().make("Toyota").model("Corolla").build();
        CarEntity car2 = CarEntity.builder().make("Honda").model("Civic").build();

        CityEntity city1 = CityEntity.builder().name("Amsterdam").build();
        CityEntity city2 = CityEntity.builder().name("Rotterdam").build();

        mockBookings = List.of(
                BookingEntity.builder()
                        .car(car1)
                        .distance(100.0)
                        .totalPrice(50.0)
                        .startCity(city1)
                        .endCity(city2)
                        .startDateTime(LocalDateTime.of(2023, Month.JANUARY, 1, 10, 0))
                        .build(),
                BookingEntity.builder()
                        .car(car1)
                        .distance(200.0)
                        .totalPrice(100.0)
                        .startCity(city2)
                        .endCity(city1)
                        .startDateTime(LocalDateTime.of(2023, Month.FEBRUARY, 1, 10, 0))
                        .build(),
                BookingEntity.builder()
                        .car(car2)
                        .distance(300.0)
                        .totalPrice(150.0)
                        .startCity(city1)
                        .endCity(city2)
                        .startDateTime(LocalDateTime.of(2023, Month.JANUARY, 1, 12, 0))
                        .build()
        );

        lenient().when(bookingRepository.findAll()).thenReturn(mockBookings);
        lenient().when(bookingRepository.findAllByCarId(1L)).thenReturn(mockBookings.subList(0, 2));
    }

    @Test
    void sumDistanceByCarId_ShouldReturnCorrectSum() {
        Double result = complexQueries.sumDistanceByCarId(1L);
        assertEquals(300.0, result);
    }

    @Test
    void sumDistances_ShouldReturnCorrectSum() {
        Double result = complexQueries.sumDistances();
        assertEquals(600.0, result);
    }

    @Test
    void sumPricesByCarId_ShouldReturnCorrectSum() {
        Double result = complexQueries.sumPricesByCarId(1L);
        assertEquals(150.0, result);
    }

    @Test
    void sumPrices_ShouldReturnCorrectSum() {
        Double result = complexQueries.sumPrices();
        assertEquals(300.0, result);
    }

    @Test
    void getMostPopularCars_ShouldReturnCorrectGrouping() {
        List<GroupingDto> result = complexQueries.getMostPopularCars();

        List<GroupingDto> expected = List.of(
                new GroupingDto("Toyota Corolla", 2L),
                new GroupingDto("Honda Civic", 1L)
        );

        assertEquals(expected, result);
    }

    @Test
    void getMostPopularTrips_ShouldReturnCorrectGrouping() {
        List<GroupingDto> result = complexQueries.getMostPopularTrips();

        List<GroupingDto> expected = List.of(
                new GroupingDto("Rotterdam -> Amsterdam", 1L),
                new GroupingDto("Amsterdam -> Rotterdam", 2L)
        );

        assertEquals(expected, result);
    }

    @Test
    void getBookingsPerMonth_ShouldReturnCorrectGrouping() {
        List<GroupingDto> result = complexQueries.getBookingsPerMonth();

        List<GroupingDto> expected = List.of(
                new GroupingDto("February", 1L),
                new GroupingDto("January", 2L)
        );

        assertEquals(expected, result);
    }
}