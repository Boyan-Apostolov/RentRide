package nl.fontys.s3.rentride_be.business.use_cases.complex_queries;

public interface ComplexBookingRepositoryQueriesUseCase {
    Double sumDistanceByCarId(Long carId);

    Double sumDistances();

    Double sumPricesByCarId(Long carId);

    Double sumPrices();
}
