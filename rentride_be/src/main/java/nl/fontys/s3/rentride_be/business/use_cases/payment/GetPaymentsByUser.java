package nl.fontys.s3.rentride_be.business.use_cases.payment;

import nl.fontys.s3.rentride_be.domain.payment.Payment;

import java.util.List;

public interface GetPaymentsByUser {
    List<Payment> getPaymentsByUser(Long userId);

    List<Payment> getPaymentsBySessionUser();

    Long getCount();

    List<Payment> getPaymentsBySessionUser(int page);
}
