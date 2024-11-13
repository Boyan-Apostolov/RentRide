package nl.fontys.s3.rentride_be.business.impl.payment;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.use_cases.payment.SetPaymentToPaid;
import nl.fontys.s3.rentride_be.persistance.PaymentRepository;
import nl.fontys.s3.rentride_be.persistance.entity.PaymentEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class SetPaymentToPaidImpl implements SetPaymentToPaid {
    private PaymentRepository paymentRepository;

    @Override
    public void setPaymentToPaid(Long paymentId) {
        Optional<PaymentEntity> payment = paymentRepository.findById(paymentId);
        if (payment.isEmpty()) throw new NotFoundException("Payment not found");

        PaymentEntity paymentEntity = payment.get();
        paymentEntity.setPaid(true);
        paymentRepository.save(paymentEntity);
    }
}
