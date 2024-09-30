package nl.fontys.s3.rentride_be.business.useCases.payment;

import com.stripe.exception.StripeException;

public interface CreatePaymentSessionUseCase {
    String createPaymentSession(String description, Long price) throws StripeException;
}
