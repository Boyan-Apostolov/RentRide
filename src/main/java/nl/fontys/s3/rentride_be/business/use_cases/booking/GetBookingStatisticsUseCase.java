package nl.fontys.s3.rentride_be.business.use_cases.booking;

import nl.fontys.s3.rentride_be.domain.statistics.StatisticsByCarResponse;

public interface GetBookingStatisticsUseCase {
    Long getTotalBookingsCountByCar(Long carId);
    Double getTotalDistanceByCar(Long carId);
    Double getTotalRevenueByCar(Long carId);
    Double getAverageRatingByCar(Long carId);

    StatisticsByCarResponse getStatisticsByCar(Long carId);
}
