package nl.fontys.s3.rentride_be.business.impl.review;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.review.GetReviewsByCar;
import nl.fontys.s3.rentride_be.domain.review.Review;
import nl.fontys.s3.rentride_be.persistance.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetReviewsByCarImpl implements GetReviewsByCar {
    private ReviewRepository reviewRepository;

    @Override
    public List<Review> getReviewsByCar(Long carId) {
        return reviewRepository
                .findAllByBooking_CarId(carId)
                .stream().map(ReviewConverter::convert)
                .toList();
    }
}
