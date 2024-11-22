package nl.fontys.s3.rentride_be.business.impl.booking;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.booking.GetStatisticsUseCase;
import nl.fontys.s3.rentride_be.domain.statistics.GeneralStatisticsResponse;
import nl.fontys.s3.rentride_be.domain.statistics.GroupingDto;
import nl.fontys.s3.rentride_be.domain.statistics.StatisticsByCarResponse;
import nl.fontys.s3.rentride_be.persistance.BookingRepository;
import nl.fontys.s3.rentride_be.persistance.DiscountPlanPurchaseRepository;
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
    private DiscountPlanPurchaseRepository discountPlanPurchaseRepository;

    private Long getTotalBookingsCountByCar(Long carId) {
        return this.bookingRepository.countByCarId(carId);
    }

    private Double getTotalDistanceByCar(Long carId) {
        return this.bookingRepository.getTotalDistanceByCar(carId);
    }

    private Double getTotalDistance() {
        return this.bookingRepository.getTotalDistance();
    }

    private Double getTotalRevenueByCar(Long carId) {
        return this.bookingRepository.getTotalPriceByCar(carId);
    }

    private Double getTotalRevenue() {
        return this.bookingRepository.getTotalPrice();
    }

    private Double getAverageRatingByCar(Long carId) {
        return this.reviewRepository.getAverageRatingsByCar(carId);
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
        return mapObjectToGroupingDto( this.discountPlanPurchaseRepository.getMostBoughtDiscountPlans());
    }

    @Override
    public List<GroupingDto> getMostPopularCars() {
        return mapObjectToGroupingDto(this.bookingRepository.getMostPopularCars());
    }

    @Override
    public List<GroupingDto> getMostPopularTrips() {
        return mapObjectToGroupingDto(this.bookingRepository.getMostPopularTrips());
    }

    @Override
    public List<GroupingDto> getBookingsPerMonth() {
        return mapObjectToGroupingDto(this.bookingRepository.getBookingsPerMonth());
    }

    private List<GroupingDto> mapObjectToGroupingDto(List<Object[]> object){
        return object
                .stream()
                .map(row -> new GroupingDto((String) row[0], ((Number) row[1]).longValue()))
                .toList();
    }
}
