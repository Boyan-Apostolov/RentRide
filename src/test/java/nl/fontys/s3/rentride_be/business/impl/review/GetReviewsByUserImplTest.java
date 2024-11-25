package nl.fontys.s3.rentride_be.business.impl.review;

import nl.fontys.s3.rentride_be.configuration.security.token.AccessToken;
import nl.fontys.s3.rentride_be.domain.review.Review;
import nl.fontys.s3.rentride_be.persistance.ReviewRepository;
import nl.fontys.s3.rentride_be.persistance.entity.ReviewEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetReviewsByUserImplTest {
    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private AccessToken accessToken;

    @InjectMocks
    private GetReviewsByUserImpl getReviewsByUser;

    @Test
    void getReviewsByUser_shouldReturnReviewsForCurrentUser() {
        long userId = 1L;
        when(accessToken.getUserId()).thenReturn(userId);

        ReviewEntity mockReviewEntity1 = ReviewEntity.builder().id(1L).text("Great!").user(null).booking(null).carSpeed(1).carCondition(1).valueForMoney(1).build();
        ReviewEntity mockReviewEntity2 = ReviewEntity.builder().id(2L).text("Good!").user(null).booking(null).carSpeed(1).carCondition(1).valueForMoney(1).build();

        when(reviewRepository.findAllByUser_Id(userId)).thenReturn(List.of(mockReviewEntity1, mockReviewEntity2));

        List<Review> expectedReviews = List.of(
                Review.builder().id(1L).text("Great!").user(null).booking(null).carSpeed(1).carCondition(1).valueForMoney(1).build(),
                Review.builder().id(2L).text("Good!").user(null).booking(null).carSpeed(1).carCondition(1).valueForMoney(1).build()
        );

        List<Review> result = getReviewsByUser.getReviewsByUser();

        assertThat(result).isEqualTo(expectedReviews);
        verify(accessToken).getUserId();
        verify(reviewRepository).findAllByUser_Id(userId);
    }

    @Test
    void getReviewsByUser_shouldReturnEmptyListWhenNoReviews() {
        long userId = 1L;
        when(accessToken.getUserId()).thenReturn(userId);
        when(reviewRepository.findAllByUser_Id(userId)).thenReturn(List.of());

        List<Review> result = getReviewsByUser.getReviewsByUser();

        assertThat(result).isEmpty();
        verify(accessToken).getUserId();
        verify(reviewRepository).findAllByUser_Id(userId);
    }
}