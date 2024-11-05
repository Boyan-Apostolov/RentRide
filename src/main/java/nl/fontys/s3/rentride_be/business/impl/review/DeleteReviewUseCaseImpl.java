package nl.fontys.s3.rentride_be.business.impl.review;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.use_cases.review.DeleteReviewUseCase;
import nl.fontys.s3.rentride_be.persistance.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteReviewUseCaseImpl implements DeleteReviewUseCase {
    private ReviewRepository reviewRepository;
    @Override
    public void deleteReview(Long reviewId) {
        if(!reviewRepository.existsById(reviewId)) {
            throw new NotFoundException("Delete->Review");
        }

        reviewRepository.deleteById(reviewId);
    }
}
