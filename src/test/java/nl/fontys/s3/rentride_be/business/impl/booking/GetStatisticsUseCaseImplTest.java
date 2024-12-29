package nl.fontys.s3.rentride_be.business.impl.booking;

import nl.fontys.s3.rentride_be.domain.statistics.GeneralStatisticsResponse;
import nl.fontys.s3.rentride_be.domain.statistics.GroupingDto;
import nl.fontys.s3.rentride_be.domain.statistics.PopularCarOverTimeDto;
import nl.fontys.s3.rentride_be.domain.statistics.StatisticsByCarResponse;
import nl.fontys.s3.rentride_be.persistance.BookingRepository;
import nl.fontys.s3.rentride_be.persistance.DiscountPlanPurchaseRepository;
import nl.fontys.s3.rentride_be.persistance.ReviewRepository;
import nl.fontys.s3.rentride_be.persistance.UserRepository;
import org.assertj.core.api.CollectionAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
    private DiscountPlanPurchaseRepository discountPlanPurchaseRepository;

    @InjectMocks
    private GetStatisticsUseCaseImpl getStatisticsUseCase;

    @Test
    void getStatisticsByCar_shouldReturnCorrectStatistics() {
        Long carId = 1L;

        when(bookingRepository.countByCarId(carId)).thenReturn(10L);
        when(bookingRepository.getTotalDistanceByCar(carId)).thenReturn(500.0);
        when(bookingRepository.getTotalPriceByCar(carId)).thenReturn(1500.0);
        when(reviewRepository.getAverageRatingsByCar(carId)).thenReturn(4.5);

        StatisticsByCarResponse expectedResponse = StatisticsByCarResponse.builder()
                .totalBookings(10L)
                .totalDistance(500.0)
                .totalRevenue(1500.0)
                .averageRating(4.5)
                .build();

        StatisticsByCarResponse actualResponse = getStatisticsUseCase.getStatisticsByCar(carId);

        assertThat(actualResponse).isEqualTo(expectedResponse);

        verify(bookingRepository).countByCarId(carId);
        verify(bookingRepository).getTotalDistanceByCar(carId);
        verify(bookingRepository).getTotalPriceByCar(carId);
        verify(reviewRepository).getAverageRatingsByCar(carId);
    }

    @Test
    void getGeneralStatistics_shouldReturnCorrectData() {
        when(bookingRepository.count()).thenReturn(100L);
        when(bookingRepository.getTotalPrice()).thenReturn(50000.0);
        when(bookingRepository.getTotalDistance()).thenReturn(20000.0);
        when(userRepository.count()).thenReturn(50L);
        when(reviewRepository.count()).thenReturn(30L);

        GeneralStatisticsResponse expectedResponse = GeneralStatisticsResponse.builder()
                .totalBookings(100L)
                .totalRevenue(50000.0)
                .totalTravelDistance(20000.0)
                .totalUsers(50L)
                .totalReviews(30L)
                .build();

        GeneralStatisticsResponse actualResponse = getStatisticsUseCase.getGeneralStatistics();

        assertThat(actualResponse).isEqualTo(expectedResponse);

        verify(bookingRepository).count();
        verify(bookingRepository).getTotalPrice();
        verify(bookingRepository).getTotalDistance();
        verify(userRepository).count();
        verify(reviewRepository).count();
    }

    @Test
    void getMostBoughtDiscountPlans_shouldReturnCorrectGrouping() {
        List<Object[]> mockData = List.of(
                new Object[]{"Plan A", 10L},
                new Object[]{"Plan B", 20L}
        );

        when(discountPlanPurchaseRepository.getMostBoughtDiscountPlans()).thenReturn(mockData);

        List<GroupingDto> expectedResult = List.of(
                new GroupingDto("Plan A", 10L),
                new GroupingDto("Plan B", 20L)
        );

        List<GroupingDto> actualResult = getStatisticsUseCase.getMostBoughtDiscountPlans();

        assertThat(actualResult).isEqualTo(expectedResult);

        verify(discountPlanPurchaseRepository).getMostBoughtDiscountPlans();
    }

    @Test
    void getMostPopularCars_shouldReturnCorrectGrouping() {
        List<Object[]> mockData = List.of(
                new Object[]{"Car A", 15L},
                new Object[]{"Car B", 25L}
        );

        when(bookingRepository.getMostPopularCars()).thenReturn(mockData);

        List<GroupingDto> expectedResult = List.of(
                new GroupingDto("Car A", 15L),
                new GroupingDto("Car B", 25L)
        );

        List<GroupingDto> actualResult = getStatisticsUseCase.getMostPopularCars();

        assertThat(actualResult).isEqualTo(expectedResult);

        verify(bookingRepository).getMostPopularCars();
    }

    @Test
    void getPopularCarsOverTime_shouldReturnCorrectData() {
        List<Object[]> mockData = List.of(
                new Object[]{"Car A", 2023, "January", 10L},
                new Object[]{"Car B", 2023, "February", 15L}
        );

        when(bookingRepository.getPopularCarsOverTime(null, null)).thenReturn(mockData);

        List<PopularCarOverTimeDto> expectedResult = List.of(
                new PopularCarOverTimeDto("Car A", 2023, "January", 10L),
                new PopularCarOverTimeDto("Car B", 2023, "February", 15L)
        );

        List<PopularCarOverTimeDto> actualResult = getStatisticsUseCase.getPopularCarsOverTime(null, null);

        CollectionAssert.assertThatCollection(actualResult).isEqualTo(expectedResult);

        verify(bookingRepository).getPopularCarsOverTime(null, null);
    }
}