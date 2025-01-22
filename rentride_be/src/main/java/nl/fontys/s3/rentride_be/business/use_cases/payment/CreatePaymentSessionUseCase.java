package nl.fontys.s3.rentride_be.business.use_cases.payment;

import com.stripe.exception.StripeException;

public interface CreatePaymentSessionUseCase {
    String createPaymentSession(String description, Double price, String paymentType, Long relatedEntityId) throws StripeException;
}
