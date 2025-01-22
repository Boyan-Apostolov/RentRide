package nl.fontys.s3.rentride_be.business.impl.review;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.InvalidOperationException;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.use_cases.review.CreateReviewUseCase;
import nl.fontys.s3.rentride_be.domain.review.CreateReviewRequest;
import nl.fontys.s3.rentride_be.persistance.BookingRepository;
import nl.fontys.s3.rentride_be.persistance.ReviewRepository;
import nl.fontys.s3.rentride_be.persistance.UserRepository;
import nl.fontys.s3.rentride_be.persistance.entity.BookingEntity;
import nl.fontys.s3.rentride_be.persistance.entity.BookingStatus;
import nl.fontys.s3.rentride_be.persistance.entity.ReviewEntity;
import nl.fontys.s3.rentride_be.persistance.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CreateReviewUseCaseImpl implements CreateReviewUseCase {
    private BookingRepository bookingRepository;
    private UserRepository userRepository;
    private ReviewRepository reviewRepository;

    @Override
    public void createReview(CreateReviewRequest request) {

        Optional<BookingEntity> booking = bookingRepository.findById(request.getBookingId());
        if(booking.isEmpty()) throw new NotFoundException("AddReview->Booking");
        BookingEntity bookingEntity = booking.get();

        if(bookingEntity.getStatus() != BookingStatus.Finished)
            throw new InvalidOperationException("Booking already rated");

        Optional<UserEntity> user = userRepository.findById(request.getUserId());
        if(user.isEmpty()) throw new NotFoundException("AddReview->User");
        UserEntity userEntity = user.get();

        ReviewEntity reviewEntity = ReviewEntity
                .builder()
                .text(request.getText())
                .createdOn(LocalDateTime.now())
                .user(userEntity)
                .booking(bookingEntity)
                .carSpeed(request.getCarSpeed())
                .carCondition(request.getCarCondition())
                .valueForMoney(request.getValueForMoney())
                .build();

        reviewRepository.save(reviewEntity);

        bookingEntity.setStatus(BookingStatus.Rated);
        bookingRepository.save(bookingEntity);
    }
}
