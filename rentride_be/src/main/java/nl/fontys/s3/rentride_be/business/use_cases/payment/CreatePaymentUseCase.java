package nl.fontys.s3.rentride_be.business.use_cases.payment;

import nl.fontys.s3.rentride_be.domain.payment.CreatePaymentRequest;
import nl.fontys.s3.rentride_be.persistance.entity.PaymentEntity;

public interface CreatePaymentUseCase {
    PaymentEntity createPayment(CreatePaymentRequest createPaymentRequest);
}
