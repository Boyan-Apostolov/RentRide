package nl.fontys.s3.rentride_be.business.impl.complex_queries;

import nl.fontys.s3.rentride_be.business.impl.complex_queries.ComplexReviewRepositoryQueriesUseCaseImpl;
import nl.fontys.s3.rentride_be.persistance.ReviewRepository;
import nl.fontys.s3.rentride_be.persistance.entity.ReviewEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ComplexReviewRepositoryQueriesUseCaseImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ComplexReviewRepositoryQueriesUseCaseImpl complexQueries;

    private List<ReviewEntity> mockReviews;

    @BeforeEach
    void setUp() {
        mockReviews = List.of(
                ReviewEntity.builder()
                        .carCondition(8)
                        .carSpeed(7)
                        .valueForMoney(9)
                        .build(),
                ReviewEntity.builder()
                        .carCondition(6)
                        .carSpeed(5)
                        .valueForMoney(7)
                        .build(),
                ReviewEntity.builder()
                        .carCondition(10)
                        .carSpeed(9)
                        .valueForMoney(8)
                        .build()
        );
    }

    @Test
    void avgRatingsByCarId_ShouldReturnCorrectAverage() {
        Long carId = 1L;

        when(reviewRepository.findAllByBooking_CarId(carId)).thenReturn(mockReviews);

        Double result = complexQueries.avgRatingsByCarId(carId);

        // Average of (8+7+9)/3, (6+5+7)/3, (10+9+8)/3
        double expectedAverage = ((8 + 7 + 9) / 3.0 + (6 + 5 + 7) / 3.0 + (10 + 9 + 8) / 3.0) / 3.0;

        assertEquals(expectedAverage, result);
    }

    @Test
    void avgRatingsByCarId_ShouldReturnZero_WhenNoReviews() {
        Long carId = 2L;

        when(reviewRepository.findAllByBooking_CarId(carId)).thenReturn(List.of());

        Double result = complexQueries.avgRatingsByCarId(carId);

        assertEquals(0.0, result);
    }
}