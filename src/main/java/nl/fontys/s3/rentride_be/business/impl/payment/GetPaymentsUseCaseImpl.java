package nl.fontys.s3.rentride_be.business.impl.payment;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.payment.GetPaymentsUseCase;
import nl.fontys.s3.rentride_be.domain.payment.Payment;
import nl.fontys.s3.rentride_be.persistance.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetPaymentsUseCaseImpl implements GetPaymentsUseCase {
    private PaymentRepository paymentRepository;

    @Override
    public List<Payment> getPayments() {
        return this.paymentRepository.findAll()
                .stream().map(PaymentConverter::convert)
                .toList();
    }
}
