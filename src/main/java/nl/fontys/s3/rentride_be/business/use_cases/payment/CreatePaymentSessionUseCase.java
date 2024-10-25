package nl.fontys.s3.rentride_be.business.use_cases.payment;

import com.stripe.exception.StripeException;
import nl.fontys.s3.rentride_be.domain.booking.Booking;

public interface CreatePaymentSessionUseCase {
    String createPaymentSession(String description, Booking booking) throws StripeException;
}
