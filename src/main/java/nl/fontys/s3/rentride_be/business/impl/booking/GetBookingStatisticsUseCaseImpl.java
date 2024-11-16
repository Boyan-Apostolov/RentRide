package nl.fontys.s3.rentride_be.business.impl.booking;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.booking.GetBookingStatisticsUseCase;
import nl.fontys.s3.rentride_be.domain.statistics.StatisticsByCarResponse;
import nl.fontys.s3.rentride_be.persistance.BookingRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetBookingStatisticsUseCaseImpl implements GetBookingStatisticsUseCase {
    private BookingRepository bookingRepository;

    @Override
    public Long getTotalBookingsCountByCar(Long carId) {
        return this.bookingRepository.countByCarId(carId);
    }

    @Override
    public Double getTotalDistanceByCar(Long carId) {
        return this.bookingRepository.sumDistanceByCarId(carId);
    }

    @Override
    public Double getTotalRevenueByCar(Long carId) {
        return this.bookingRepository.sumPricesByCarId(carId);
    }

    @Override
    public Double getAverageRatingByCar(Long carId) {
        return this.bookingRepository.avgRatingsByCarId(carId);
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
}
