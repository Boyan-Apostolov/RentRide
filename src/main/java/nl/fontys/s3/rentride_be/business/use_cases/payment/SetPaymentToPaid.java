package nl.fontys.s3.rentride_be.business.use_cases.payment;

import nl.fontys.s3.rentride_be.domain.payment.Payment;

public interface SetPaymentToPaid {
    void setPaymentToPaid(Long paymentId);
}
