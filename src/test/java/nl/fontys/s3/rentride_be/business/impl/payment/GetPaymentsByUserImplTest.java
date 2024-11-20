package nl.fontys.s3.rentride_be.business.impl.payment;

import nl.fontys.s3.rentride_be.domain.payment.Payment;
import nl.fontys.s3.rentride_be.persistance.PaymentRepository;
import nl.fontys.s3.rentride_be.persistance.entity.PaymentEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetPaymentsByUserImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private GetPaymentsByUserImpl getPaymentsByUser;

    private PaymentEntity paymentEntity1;
    private PaymentEntity paymentEntity2;
    private Payment payment1;
    private Payment payment2;

    @BeforeEach
    void setUp() {
        paymentEntity1 = PaymentEntity.builder()
                .id(1L)
                .amount(100.0)
                .description("Payment 1")
                .createdOn(LocalDateTime.now())
                .build();

        paymentEntity2 = PaymentEntity.builder()
                .id(2L)
                .amount(200.0)
                .description("Payment 2")
                .createdOn(LocalDateTime.now())
                .build();

        payment1 = PaymentConverter.convert(paymentEntity1);
        payment2 = PaymentConverter.convert(paymentEntity2);
    }

    @Test
    void getPaymentsByUser_ShouldReturnPaymentsForUser() {
        Long userId = 1L;
        when(paymentRepository.findAllByUserId(userId)).thenReturn(List.of(paymentEntity1, paymentEntity2));

        List<Payment> result = getPaymentsByUser.getPaymentsByUser(userId);

        assertEquals(2, result.size());
        assertEquals(payment1, result.get(0));
        assertEquals(payment2, result.get(1));

        verify(paymentRepository, times(1)).findAllByUserId(userId);
    }

    @Test
    void getPaymentsByUser_ShouldReturnEmptyList_WhenNoPaymentsExist() {
        Long userId = 1L;
        when(paymentRepository.findAllByUserId(userId)).thenReturn(List.of());

        List<Payment> result = getPaymentsByUser.getPaymentsByUser(userId);

        assertEquals(0, result.size());

        verify(paymentRepository, times(1)).findAllByUserId(userId);
    }
}