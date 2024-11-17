package nl.fontys.s3.rentride_be.business.impl.complex_queries;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.complex_queries.ComplexReviewRepositoryQueriesUseCase;
import nl.fontys.s3.rentride_be.persistance.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ComplexReviewRepositoryQueriesUseCaseImpl implements ComplexReviewRepositoryQueriesUseCase {
    private ReviewRepository reviewRepository;

    public Double avgRatingsByCarId(Long carId) {
        return reviewRepository.findAllByBooking_CarId(carId).stream()
                .mapToDouble(review -> (review.getCarCondition() + review.getCarSpeed() + review.getValueForMoney()) / 3.0)
                .average()
                .orElse(0.0);
    }
}
