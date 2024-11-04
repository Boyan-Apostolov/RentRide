package nl.fontys.s3.rentride_be.business.impl.review;

import nl.fontys.s3.rentride_be.business.impl.booking.BookingConverter;
import nl.fontys.s3.rentride_be.business.impl.user.UserConverter;
import nl.fontys.s3.rentride_be.domain.review.Review;
import nl.fontys.s3.rentride_be.persistance.entity.ReviewEntity;

public class ReviewConverter {
    private ReviewConverter() {
    }

    public static Review convert(ReviewEntity reviewEntity) {
        if (reviewEntity == null) return null;

        return Review
                .builder()
                .id(reviewEntity.getId())
                .user(UserConverter.convert(reviewEntity.getUser()))
                .booking(BookingConverter.convert(reviewEntity.getBooking()))
                .carCondition(reviewEntity.getCarCondition())
                .valueForMoney(reviewEntity.getValueForMoney())
                .carSpeed(reviewEntity.getCarSpeed())
                .text(reviewEntity.getText())
                .createdOn(reviewEntity.getCreatedOn())
                .build();
    }
}
