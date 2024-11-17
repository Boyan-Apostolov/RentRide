package nl.fontys.s3.rentride_be.business.use_cases.booking;

import nl.fontys.s3.rentride_be.domain.statistics.GeneralStatisticsResponse;
import nl.fontys.s3.rentride_be.domain.statistics.GroupingDto;
import nl.fontys.s3.rentride_be.domain.statistics.StatisticsByCarResponse;

import java.util.List;

public interface GetStatisticsUseCase {
    StatisticsByCarResponse getStatisticsByCar(Long carId);
    GeneralStatisticsResponse getGeneralStatistics();
    List<GroupingDto> getMostBoughtDiscountPlans();
    List<GroupingDto> getMostPopularCars();

    List<GroupingDto> getMostPopularTrips();

    List<GroupingDto> getBookingsPerMonth();
}
