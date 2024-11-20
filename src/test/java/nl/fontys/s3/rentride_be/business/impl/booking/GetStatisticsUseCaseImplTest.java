package nl.fontys.s3.rentride_be.business.impl.booking;

import nl.fontys.s3.rentride_be.business.use_cases.complex_queries.ComplexBookingRepositoryQueriesUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.complex_queries.ComplexDiscountPlanRepositoryQueriesUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.complex_queries.ComplexReviewRepositoryQueriesUseCase;
import nl.fontys.s3.rentride_be.domain.statistics.GeneralStatisticsResponse;
import nl.fontys.s3.rentride_be.domain.statistics.GroupingDto;
import nl.fontys.s3.rentride_be.domain.statistics.StatisticsByCarResponse;
import nl.fontys.s3.rentride_be.persistance.BookingRepository;
import nl.fontys.s3.rentride_be.persistance.ReviewRepository;
import nl.fontys.s3.rentride_be.persistance.UserRepository;
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
class GetStatisticsUseCaseImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ComplexBookingRepositoryQueriesUseCase complexBookingRepositoryQueriesUseCase;

    @Mock
    private ComplexReviewRepositoryQueriesUseCase complexReviewRepositoryQueriesUseCase;

    @Mock
    private ComplexDiscountPlanRepositoryQueriesUseCase complexDiscountPlanRepositoryQueriesUseCase;

    @InjectMocks
    private GetStatisticsUseCaseImpl getStatisticsUseCase;

    @BeforeEach
    void setUp() {
        // Setup common mocks if required
    }

    @Test
    void getStatisticsByCar_ShouldReturnCorrectStatistics() {
        Long carId = 1L;

        when(complexBookingRepositoryQueriesUseCase.sumDistanceByCarId(carId)).thenReturn(1500.0);
        when(complexBookingRepositoryQueriesUseCase.sumPricesByCarId(carId)).thenReturn(5000.0);
        when(complexReviewRepositoryQueriesUseCase.avgRatingsByCarId(carId)).thenReturn(4.5);
        when(bookingRepository.countByCarId(carId)).thenReturn(20L);

        StatisticsByCarResponse response = getStatisticsUseCase.getStatisticsByCar(carId);

        assertEquals(1500.0, response.getTotalDistance());
        assertEquals(5000.0, response.getTotalRevenue());
        assertEquals(4.5, response.getAverageRating());
        assertEquals(20L, response.getTotalBookings());

        verify(complexBookingRepositoryQueriesUseCase).sumDistanceByCarId(carId);
        verify(complexBookingRepositoryQueriesUseCase).sumPricesByCarId(carId);
        verify(complexReviewRepositoryQueriesUseCase).avgRatingsByCarId(carId);
        verify(bookingRepository).countByCarId(carId);
    }

    @Test
    void getGeneralStatistics_ShouldReturnCorrectStatistics() {
        when(bookingRepository.count()).thenReturn(100L);
        when(userRepository.count()).thenReturn(50L);
        when(reviewRepository.count()).thenReturn(25L);
        when(complexBookingRepositoryQueriesUseCase.sumDistances()).thenReturn(10000.0);
        when(complexBookingRepositoryQueriesUseCase.sumPrices()).thenReturn(20000.0);

        GeneralStatisticsResponse response = getStatisticsUseCase.getGeneralStatistics();

        assertEquals(100L, response.getTotalBookings());
        assertEquals(50L, response.getTotalUsers());
        assertEquals(25L, response.getTotalReviews());
        assertEquals(10000.0, response.getTotalTravelDistance());
        assertEquals(20000.0, response.getTotalRevenue());

        verify(bookingRepository).count();
        verify(userRepository).count();
        verify(reviewRepository).count();
        verify(complexBookingRepositoryQueriesUseCase).sumDistances();
        verify(complexBookingRepositoryQueriesUseCase).sumPrices();
    }

    @Test
    void getMostBoughtDiscountPlans_ShouldReturnCorrectGrouping() {
        List<GroupingDto> mockData = List.of(
                new GroupingDto("Plan A", 5L),
                new GroupingDto("Plan B", 3L)
        );
        when(complexDiscountPlanRepositoryQueriesUseCase.getMostBoughtDiscountPlans()).thenReturn(mockData);

        List<GroupingDto> result = getStatisticsUseCase.getMostBoughtDiscountPlans();

        assertEquals(mockData, result);
        verify(complexDiscountPlanRepositoryQueriesUseCase).getMostBoughtDiscountPlans();
    }

    @Test
    void getMostPopularCars_ShouldReturnCorrectGrouping() {
        List<GroupingDto> mockData = List.of(
                new GroupingDto("Car A", 10L),
                new GroupingDto("Car B", 7L)
        );
        when(complexBookingRepositoryQueriesUseCase.getMostPopularCars()).thenReturn(mockData);

        List<GroupingDto> result = getStatisticsUseCase.getMostPopularCars();

        assertEquals(mockData, result);
        verify(complexBookingRepositoryQueriesUseCase).getMostPopularCars();
    }

    @Test
    void getMostPopularTrips_ShouldReturnCorrectGrouping() {
        List<GroupingDto> mockData = List.of(
                new GroupingDto("City A -> City B", 12L),
                new GroupingDto("City C -> City D", 8L)
        );
        when(complexBookingRepositoryQueriesUseCase.getMostPopularTrips()).thenReturn(mockData);

        List<GroupingDto> result = getStatisticsUseCase.getMostPopularTrips();

        assertEquals(mockData, result);
        verify(complexBookingRepositoryQueriesUseCase).getMostPopularTrips();
    }

    @Test
    void getBookingsPerMonth_ShouldReturnCorrectGrouping() {
        List<GroupingDto> mockData = List.of(
                new GroupingDto("January", 15L),
                new GroupingDto("February", 10L)
        );
        when(complexBookingRepositoryQueriesUseCase.getBookingsPerMonth()).thenReturn(mockData);

        List<GroupingDto> result = getStatisticsUseCase.getBookingsPerMonth();

        assertEquals(mockData, result);
        verify(complexBookingRepositoryQueriesUseCase).getBookingsPerMonth();
    }
}