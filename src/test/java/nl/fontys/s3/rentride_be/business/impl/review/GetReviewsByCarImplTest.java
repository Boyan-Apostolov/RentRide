package nl.fontys.s3.rentride_be.business.impl.review;

import nl.fontys.s3.rentride_be.domain.review.Review;
import nl.fontys.s3.rentride_be.persistance.ReviewRepository;
import nl.fontys.s3.rentride_be.persistance.entity.ReviewEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetReviewsByCarImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private GetReviewsByCarImpl getReviewsByCar;

    private ReviewEntity reviewEntity1;
    private ReviewEntity reviewEntity2;
    private Review review1;
    private Review review2;

    @BeforeEach
    void setUp() {
        reviewEntity1 = ReviewEntity.builder()
                .id(1L)
                .text("Review 1")
                .carCondition(4)
                .carSpeed(5)
                .valueForMoney(3)
                .createdOn(LocalDateTime.now())
                .build();

        reviewEntity2 = ReviewEntity.builder()
                .id(2L)
                .text("Review 2")
                .carCondition(5)
                .carSpeed(4)
                .valueForMoney(4)
                .createdOn(LocalDateTime.now())
                .build();

        review1 = ReviewConverter.convert(reviewEntity1);
        review2 = ReviewConverter.convert(reviewEntity2);
    }

    @Test
    void getReviewsByCar_ShouldReturnReviewsForCar() {
        Long carId = 1L;
        when(reviewRepository.findAllByBooking_CarId(carId)).thenReturn(List.of(reviewEntity1, reviewEntity2));

        List<Review> result = getReviewsByCar.getReviewsByCar(carId);

        assertEquals(2, result.size());
        assertEquals(review1, result.get(0));
        assertEquals(review2, result.get(1));

        verify(reviewRepository, times(1)).findAllByBooking_CarId(carId);
    }

    @Test
    void getReviewsByCar_ShouldReturnEmptyList_WhenNoReviewsExist() {
        Long carId = 1L;
        when(reviewRepository.findAllByBooking_CarId(carId)).thenReturn(List.of());

        List<Review> result = getReviewsByCar.getReviewsByCar(carId);

        assertEquals(0, result.size());

        verify(reviewRepository, times(1)).findAllByBooking_CarId(carId);
    }
}