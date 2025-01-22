package nl.fontys.s3.rentride_be.controller;

import nl.fontys.s3.rentride_be.business.use_cases.review.CreateReviewUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.review.DeleteReviewUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.review.GetReviewsByCar;
import nl.fontys.s3.rentride_be.business.use_cases.review.UpdateReviewUseCase;
import nl.fontys.s3.rentride_be.domain.review.CreateReviewRequest;
import nl.fontys.s3.rentride_be.domain.review.Review;
import nl.fontys.s3.rentride_be.domain.review.UpdateReviewRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewsControllerTest {

    @Mock
    private GetReviewsByCar getReviewsByCar;

    @Mock
    private DeleteReviewUseCase deleteReviewUseCase;

    @Mock
    private UpdateReviewUseCase updateReviewUseCase;

    @Mock
    private CreateReviewUseCase createReviewUseCase;

    @InjectMocks
    private ReviewsController reviewsController;

    private Review review;

    @BeforeEach
    void setUp() {
        review = Review.builder()
                .id(1L)
                .text("Great car!")
                .carCondition(5)
                .carSpeed(4)
                .valueForMoney(4)
                .build();
    }

    @Test
    void getReviews_ShouldReturnReviewsForCar() {
        Long carId = 1L;
        when(getReviewsByCar.getReviewsByCar(carId)).thenReturn(List.of(review));

        ResponseEntity<List<Review>> response = reviewsController.getReviews(carId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(review, response.getBody().get(0));

        verify(getReviewsByCar, times(1)).getReviewsByCar(carId);
    }

    @Test
    void deleteReview_ShouldDeleteReviewById() {
        Long reviewId = 1L;

        ResponseEntity<Void> response = reviewsController.deleteReview(reviewId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(deleteReviewUseCase, times(1)).deleteReview(reviewId);
    }

    @Test
    void updateReview_ShouldUpdateReview() {
        Long reviewId = 1L;
        UpdateReviewRequest request = UpdateReviewRequest.builder()
                .text("Updated review")
                .carCondition(4)
                .carSpeed(5)
                .valueForMoney(3)
                .build();

        ResponseEntity<Void> response = reviewsController.updateReview(reviewId, request);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(updateReviewUseCase, times(1)).updateReview(request);
        assertEquals(reviewId, request.getId());
    }

    @Test
    void createReview_ShouldCreateReview() {
        CreateReviewRequest request = CreateReviewRequest.builder()
                .text("New review")
                .carCondition(5)
                .carSpeed(4)
                .valueForMoney(4)
                .build();

        ResponseEntity<Void> response = reviewsController.createReview(request);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(createReviewUseCase, times(1)).createReview(request);
    }
}