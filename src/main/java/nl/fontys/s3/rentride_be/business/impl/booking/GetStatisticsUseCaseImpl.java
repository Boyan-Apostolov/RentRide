package nl.fontys.s3.rentride_be.business.impl.booking;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.booking.GetStatisticsUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.complex_queries.ComplexBookingRepositoryQueriesUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.complex_queries.ComplexDiscountPlanRepositoryQueriesUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.complex_queries.ComplexReviewRepositoryQueriesUseCase;
import nl.fontys.s3.rentride_be.domain.statistics.GeneralStatisticsResponse;
import nl.fontys.s3.rentride_be.domain.statistics.GroupingDto;
import nl.fontys.s3.rentride_be.domain.statistics.StatisticsByCarResponse;
import nl.fontys.s3.rentride_be.persistance.BookingRepository;
import nl.fontys.s3.rentride_be.persistance.ReviewRepository;
import nl.fontys.s3.rentride_be.persistance.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetStatisticsUseCaseImpl implements GetStatisticsUseCase {
    private BookingRepository bookingRepository;
    private UserRepository userRepository;
    private ReviewRepository reviewRepository;

    private ComplexBookingRepositoryQueriesUseCase complexBookingRepositoryQueriesUseCase;
    private ComplexReviewRepositoryQueriesUseCase complexReviewRepositoryQueriesUseCase;
    private ComplexDiscountPlanRepositoryQueriesUseCase complexDiscountPlanRepositoryQueriesUseCase;

    private Long getTotalBookingsCountByCar(Long carId) {
        return this.bookingRepository.countByCarId(carId);
    }

    private Double getTotalDistanceByCar(Long carId) {
        return this.complexBookingRepositoryQueriesUseCase.sumDistanceByCarId(carId);
    }

    private Double getTotalDistance() {
        return this.complexBookingRepositoryQueriesUseCase.sumDistances();
    }

    private Double getTotalRevenueByCar(Long carId) {
        return this.complexBookingRepositoryQueriesUseCase.sumPricesByCarId(carId);
    }

    private Double getTotalRevenue() {
        return this.complexBookingRepositoryQueriesUseCase.sumPrices();
    }

    private Double getAverageRatingByCar(Long carId) {
        return this.complexReviewRepositoryQueriesUseCase.avgRatingsByCarId(carId);
    }

    @Override
    public StatisticsByCarResponse getStatisticsByCar(Long carId) {
        return StatisticsByCarResponse.builder()
                .totalDistance(this.getTotalDistanceByCar(carId))
                .totalRevenue(this.getTotalRevenueByCar(carId))
                .averageRating(this.getAverageRatingByCar(carId))
                .totalBookings(this.getTotalBookingsCountByCar(carId))
                .build();
    }

    @Override
    public GeneralStatisticsResponse getGeneralStatistics() {
        return GeneralStatisticsResponse.builder()
                .totalBookings(bookingRepository.count())
                .totalRevenue(getTotalRevenue())
                .totalTravelDistance(getTotalDistance())
                .totalUsers(userRepository.count())
                .totalReviews(reviewRepository.count())
                .build();
    }

    @Override
    public List<GroupingDto> getMostBoughtDiscountPlans() {
        return this.complexDiscountPlanRepositoryQueriesUseCase.getMostBoughtDiscountPlans();
    }

    @Override
    public List<GroupingDto> getMostPopularCars() {
        return this.complexBookingRepositoryQueriesUseCase.getMostPopularCars();
    }

    @Override
    public List<GroupingDto> getMostPopularTrips() {
        return this.complexBookingRepositoryQueriesUseCase.getMostPopularTrips();
    }

    @Override
    public List<GroupingDto> getBookingsPerMonth() {
        return this.complexBookingRepositoryQueriesUseCase.getBookingsPerMonth();
    }
}
