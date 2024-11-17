package nl.fontys.s3.rentride_be.business.use_cases.booking;

import nl.fontys.s3.rentride_be.domain.statistics.GeneralStatisticsResponse;
import nl.fontys.s3.rentride_be.domain.statistics.StatisticsByCarResponse;

public interface GetStatisticsUseCase {
    StatisticsByCarResponse getStatisticsByCar(Long carId);
    GeneralStatisticsResponse getGeneralStatistics();
}
