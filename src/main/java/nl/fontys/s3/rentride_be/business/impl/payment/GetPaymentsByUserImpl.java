package nl.fontys.s3.rentride_be.business.impl.payment;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.payment.GetPaymentsByUser;
import nl.fontys.s3.rentride_be.configuration.security.token.AccessToken;
import nl.fontys.s3.rentride_be.domain.payment.Payment;
import nl.fontys.s3.rentride_be.persistance.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetPaymentsByUserImpl implements GetPaymentsByUser {
private PaymentRepository paymentRepository;
private AccessToken accessToken;


    @Override
    public List<Payment> getPaymentsByUser(Long userId) {
        return this.paymentRepository.findAllByUserId(userId)
                .stream().map(PaymentConverter::convert)
                .toList();
    }

    @Override
    public List<Payment> getPaymentsBySessionUser() {
        return getPaymentsByUser(accessToken.getUserId());
    }
}
