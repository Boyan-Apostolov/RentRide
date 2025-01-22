package nl.fontys.s3.rentride_be.business.use_cases.review;

import nl.fontys.s3.rentride_be.domain.review.UpdateReviewRequest;

public interface UpdateReviewUseCase {
    void updateReview (UpdateReviewRequest request);
}
