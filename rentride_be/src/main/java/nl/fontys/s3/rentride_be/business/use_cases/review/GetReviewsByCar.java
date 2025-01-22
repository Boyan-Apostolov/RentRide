package nl.fontys.s3.rentride_be.business.use_cases.review;

import nl.fontys.s3.rentride_be.domain.review.Review;

import java.util.List;

public interface GetReviewsByCar {
    List<Review> getReviewsByCar(Long carId);
}
