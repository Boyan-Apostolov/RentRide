package nl.fontys.s3.rentride_be.business.impl.payment;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.use_cases.payment.UpdatePaymentStatusUseCase;
import nl.fontys.s3.rentride_be.persistance.PaymentRepository;
import nl.fontys.s3.rentride_be.persistance.entity.PaymentEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdatePaymentStatusUseCaseImpl implements UpdatePaymentStatusUseCase {
    private PaymentRepository paymentRepository;

    @Override
    public void updatePaymentStatus(Long paymentId, boolean isPaid) {
        Optional<PaymentEntity> payment = paymentRepository.findById(paymentId);
        if (payment.isEmpty()) throw new NotFoundException("Payment not found");

        PaymentEntity paymentEntity = payment.get();
        paymentEntity.setPaid(isPaid);
        paymentRepository.save(paymentEntity);
    }
}
