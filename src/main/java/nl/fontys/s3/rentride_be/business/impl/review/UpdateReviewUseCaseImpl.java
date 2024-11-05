package nl.fontys.s3.rentride_be.business.impl.review;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.use_cases.review.UpdateReviewUseCase;
import nl.fontys.s3.rentride_be.domain.city.UpdateCityRequest;
import nl.fontys.s3.rentride_be.domain.review.UpdateReviewRequest;
import nl.fontys.s3.rentride_be.persistance.ReviewRepository;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
import nl.fontys.s3.rentride_be.persistance.entity.ReviewEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateReviewUseCaseImpl implements UpdateReviewUseCase {
    private ReviewRepository reviewRepository;
    @Override
    public void updateReview(UpdateReviewRequest request) {
        Optional<ReviewEntity> reviewEntityOptional = this.reviewRepository.findById(request.getId());
        if (reviewEntityOptional.isEmpty()) {
            throw new NotFoundException("Review");
        }

        ReviewEntity reviewEntity = reviewEntityOptional.get();
        reviewEntity.setText(request.getText());
        reviewEntity.setCarCondition(request.getCarCondition());
        reviewEntity.setCarSpeed(request.getCarSpeed());
        reviewEntity.setValueForMoney(request.getValueForMoney());

        this.reviewRepository.save(reviewEntity);
    }
}
