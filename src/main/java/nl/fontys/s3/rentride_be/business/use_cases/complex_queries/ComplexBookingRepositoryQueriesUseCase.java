package nl.fontys.s3.rentride_be.business.use_cases.complex_queries;

import nl.fontys.s3.rentride_be.domain.statistics.GroupingDto;

import java.util.List;

public interface ComplexBookingRepositoryQueriesUseCase {
    Double sumDistanceByCarId(Long carId);

    Double sumDistances();

    Double sumPricesByCarId(Long carId);

    Double sumPrices();

    List<GroupingDto> getMostPopularCars();

    List<GroupingDto> getMostPopularTrips();

    List<GroupingDto> getBookingsPerMonth();
}
