package nl.fontys.s3.rentride_be.business.impl.review;

import nl.fontys.s3.rentride_be.domain.review.Review;
import nl.fontys.s3.rentride_be.persistance.entity.ReviewEntity;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ReviewConverterTest {
    @Test
    void shouldConvertAllCountryFieldsToDomain(){
        ReviewEntity reviewToBeConverted = ReviewEntity
                .builder()
                .id(1L)
                .valueForMoney(1)
                .carCondition(1)
                .carSpeed(1)
                .text("test")
                .user(null)
                .booking(null)
                .build();

        Review actual = ReviewConverter.convert(reviewToBeConverted);

        Review expected = Review
                .builder()
                .id(1L)
                .valueForMoney(1)
                .carCondition(1)
                .carSpeed(1)
                .text("test")
                .user(null)
                .booking(null)
                .build();


        assertEquals(expected, actual);
    }

    @Test
    void converterWithNullShouldReturnNull(){
        assertNull(ReviewConverter.convert(null));
    }
}
