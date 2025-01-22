package nl.fontys.s3.rentride_be.business.impl.review;

import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.persistance.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteReviewUseCaseImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private DeleteReviewUseCaseImpl deleteReviewUseCase;

    @Test
    void deleteReview_ShouldDeleteSuccessfully_WhenReviewExists() {
        Long reviewId = 1L;

        when(reviewRepository.existsById(reviewId)).thenReturn(true);

        deleteReviewUseCase.deleteReview(reviewId);

        verify(reviewRepository, times(1)).deleteById(reviewId);
    }

    @Test
    void deleteReview_ShouldThrowNotFoundException_WhenReviewDoesNotExist() {
        Long reviewId = 1L;

        when(reviewRepository.existsById(reviewId)).thenReturn(false);

        assertThrows(NotFoundException.class, () ->
                deleteReviewUseCase.deleteReview(reviewId));

        verify(reviewRepository, never()).deleteById(any());
    }
}