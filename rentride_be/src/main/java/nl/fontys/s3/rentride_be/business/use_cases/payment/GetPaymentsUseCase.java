package nl.fontys.s3.rentride_be.business.use_cases.payment;

import nl.fontys.s3.rentride_be.domain.payment.Payment;

import java.util.List;

public interface GetPaymentsUseCase {
    List<Payment> getPayments();
}
