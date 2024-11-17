package nl.fontys.s3.rentride_be.business.use_cases.complex_queries;

public interface ComplexReviewRepositoryQueriesUseCase {
    Double avgRatingsByCarId(Long carId);
}
