package nl.fontys.s3.rentride_be.business.impl.payment;

import lombok.RequiredArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.payment.GetPaymentsByUser;
import nl.fontys.s3.rentride_be.configuration.security.token.AccessToken;
import nl.fontys.s3.rentride_be.domain.payment.Payment;
import nl.fontys.s3.rentride_be.persistance.PaymentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPaymentsByUserImpl implements GetPaymentsByUser {
    private final PaymentRepository paymentRepository;
    private final AccessToken accessToken;

    @Value("${DEFAULT_PAGE_SIZE}")
    private int DefaultPageSize;

    @Override
    public List<Payment> getPaymentsByUser(Long userId) {
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);

        return this.paymentRepository.findAllByUserId(userId, pageable)
                .stream().map(PaymentConverter::convert)
                .toList();
    }

    @Override
    public List<Payment> getPaymentsBySessionUser() {
        return getPaymentsByUser(accessToken.getUserId());
    }

    @Override
    public Long getCount() {
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);

        return this.paymentRepository.findAllByUserId(accessToken.getUserId(), pageable).stream().count();
    }

    @Override
    public List<Payment> getPaymentsBySessionUser(int page) {
        Pageable pageable = PageRequest.of(page, DefaultPageSize);

        return this.paymentRepository.findAllByUserId(accessToken.getUserId(), pageable)
                .stream().map(PaymentConverter::convert)
                .toList();
    }
}
