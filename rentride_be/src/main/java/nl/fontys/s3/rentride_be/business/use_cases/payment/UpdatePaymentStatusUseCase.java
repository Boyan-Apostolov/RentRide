package nl.fontys.s3.rentride_be.business.use_cases.payment;

public interface UpdatePaymentStatusUseCase {
    void updatePaymentStatus(Long paymentId, boolean isPaid);
}
