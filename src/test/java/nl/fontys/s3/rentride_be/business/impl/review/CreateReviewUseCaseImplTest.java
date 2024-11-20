package nl.fontys.s3.rentride_be.business.impl.review;

import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.domain.review.CreateReviewRequest;
import nl.fontys.s3.rentride_be.persistance.BookingRepository;
import nl.fontys.s3.rentride_be.persistance.ReviewRepository;
import nl.fontys.s3.rentride_be.persistance.UserRepository;
import nl.fontys.s3.rentride_be.persistance.entity.BookingEntity;
import nl.fontys.s3.rentride_be.persistance.entity.BookingStatus;
import nl.fontys.s3.rentride_be.persistance.entity.ReviewEntity;
import nl.fontys.s3.rentride_be.persistance.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateReviewUseCaseImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private CreateReviewUseCaseImpl createReviewUseCase;

    private CreateReviewRequest request;
    private BookingEntity mockBooking;
    private UserEntity mockUser;

    @BeforeEach
    void setUp() {
        request = new CreateReviewRequest();
        request.setBookingId(1L);
        request.setUserId(1L);
        request.setText("Great experience!");
        request.setCarSpeed(5);
        request.setCarCondition(4);
        request.setValueForMoney(5);

        mockBooking = new BookingEntity();
        mockBooking.setId(1L);
        mockBooking.setStatus(BookingStatus.Finished);

        mockUser = new UserEntity();
        mockUser.setId(1L);
    }

    @Test
    void createReview_ShouldSaveReviewAndUpdateBookingStatus_WhenEntitiesExist() {
        when(bookingRepository.findById(request.getBookingId())).thenReturn(Optional.of(mockBooking));
        when(userRepository.findById(request.getUserId())).thenReturn(Optional.of(mockUser));

        createReviewUseCase.createReview(request);

        verify(reviewRepository, times(1)).save(argThat(review ->
                review.getText().equals(request.getText()) &&
                        review.getCarSpeed() == request.getCarSpeed() &&
                        review.getCarCondition() == request.getCarCondition() &&
                        review.getValueForMoney() == request.getValueForMoney() &&
                        review.getUser().equals(mockUser) &&
                        review.getBooking().equals(mockBooking) &&
                        review.getCreatedOn() != null
        ));

        verify(bookingRepository, times(1)).save(argThat(booking ->
                booking.getStatus() == BookingStatus.Rated &&
                        booking.getId().equals(mockBooking.getId())
        ));
    }

    @Test
    void createReview_ShouldThrowNotFoundException_WhenBookingNotFound() {
        when(bookingRepository.findById(request.getBookingId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> createReviewUseCase.createReview(request));

        verify(bookingRepository, times(1)).findById(request.getBookingId());
        verify(userRepository, never()).findById(anyLong());
        verify(reviewRepository, never()).save(any(ReviewEntity.class));
    }

    @Test
    void createReview_ShouldThrowNotFoundException_WhenUserNotFound() {
        when(bookingRepository.findById(request.getBookingId())).thenReturn(Optional.of(mockBooking));
        when(userRepository.findById(request.getUserId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> createReviewUseCase.createReview(request));

        verify(bookingRepository, times(1)).findById(request.getBookingId());
        verify(userRepository, times(1)).findById(request.getUserId());
        verify(reviewRepository, never()).save(any(ReviewEntity.class));
    }
}