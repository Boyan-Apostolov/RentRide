package nl.fontys.s3.rentride_be.business.impl.payment;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.use_cases.payment.UpdatePaymentUseCase;
import nl.fontys.s3.rentride_be.persistance.PaymentRepository;
import nl.fontys.s3.rentride_be.persistance.entity.PaymentEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdatePaymentUseCaseImpl implements UpdatePaymentUseCase {
    private PaymentRepository paymentRepository;

    @Override
    public void updatePayment(PaymentEntity paymentEntity) {
        if(!paymentRepository.existsById(paymentEntity.getId())) throw new NotFoundException("UpdatePayment->Payment");

        this.paymentRepository.save(paymentEntity);
    }
}
