package nl.fontys.s3.rentride_be.business.impl.review;

import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.impl.review.UpdateReviewUseCaseImpl;
import nl.fontys.s3.rentride_be.domain.review.UpdateReviewRequest;
import nl.fontys.s3.rentride_be.persistance.ReviewRepository;
import nl.fontys.s3.rentride_be.persistance.entity.ReviewEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateReviewUseCaseImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private UpdateReviewUseCaseImpl updateReviewUseCase;

    @Test
    void updateReview_ShouldUpdateSuccessfully_WhenReviewExists() {
        Long reviewId = 1L;
        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.setId(reviewId);

        UpdateReviewRequest request = UpdateReviewRequest.builder()
                .id(reviewId)
                .text("Updated review text")
                .carCondition(8)
                .carSpeed(7)
                .valueForMoney(9)
                .build();

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(reviewEntity));

        updateReviewUseCase.updateReview(request);

        verify(reviewRepository, times(1)).save(reviewEntity);
        assertEquals("Updated review text", reviewEntity.getText());
        assertEquals(8, reviewEntity.getCarCondition());
        assertEquals(7, reviewEntity.getCarSpeed());
        assertEquals(9, reviewEntity.getValueForMoney());
    }

    @Test
    void updateReview_ShouldThrowNotFoundException_WhenReviewDoesNotExist() {
        Long reviewId = 1L;

        UpdateReviewRequest request = UpdateReviewRequest.builder()
                .id(reviewId)
                .text("Updated review text")
                .carCondition(8)
                .carSpeed(7)
                .valueForMoney(9)
                .build();

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> updateReviewUseCase.updateReview(request));

        verify(reviewRepository, never()).save(any());
    }
}