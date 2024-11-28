package nl.fontys.s3.rentride_be.business.impl.payment;

import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.persistance.PaymentRepository;
import nl.fontys.s3.rentride_be.persistance.entity.PaymentEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class UpdatePaymentUseCaseImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private UpdatePaymentUseCaseImpl updatePaymentUseCase;

    @Test
    void updatePayment_shouldUpdatePaymentWhenExists() {
        PaymentEntity paymentEntity = PaymentEntity.builder().id(1L).amount(100.0).build();
        when(paymentRepository.existsById(paymentEntity.getId())).thenReturn(true);

        updatePaymentUseCase.updatePayment(paymentEntity);

        verify(paymentRepository).existsById(paymentEntity.getId());
        verify(paymentRepository).save(paymentEntity);
    }

    @Test
    void updatePayment_shouldThrowNotFoundExceptionWhenPaymentDoesNotExist() {
        PaymentEntity paymentEntity = PaymentEntity.builder().id(1L).amount(100.0).build();
        when(paymentRepository.existsById(paymentEntity.getId())).thenReturn(false);

        assertThatThrownBy(() -> updatePaymentUseCase.updatePayment(paymentEntity))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("400 BAD_REQUEST \"UpdatePayment->Payment_NOT_FOUND\"");

        verify(paymentRepository).existsById(paymentEntity.getId());
        verify(paymentRepository, never()).save(any());
    }
}