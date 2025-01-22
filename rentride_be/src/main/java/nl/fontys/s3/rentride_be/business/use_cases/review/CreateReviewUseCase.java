package nl.fontys.s3.rentride_be.business.use_cases.review;
import nl.fontys.s3.rentride_be.domain.review.CreateReviewRequest;

public interface CreateReviewUseCase {
    void createReview(CreateReviewRequest request);
}
