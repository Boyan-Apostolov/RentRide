package nl.fontys.s3.rentride_be.business.impl.payment;

import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.persistance.PaymentRepository;
import nl.fontys.s3.rentride_be.persistance.entity.PaymentEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdatePaymentStatusUseCaseImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private UpdatePaymentStatusUseCaseImpl updatePaymentStatusUseCase;

    @Test
    void updatePaymentStatus_shouldUpdateStatusWhenPaymentExists() {
        Long paymentId = 1L;
        boolean isPaid = true;

        PaymentEntity paymentEntity = PaymentEntity.builder()
                .id(paymentId)
                .isPaid(false)
                .build();

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(paymentEntity));

        updatePaymentStatusUseCase.updatePaymentStatus(paymentId, isPaid);

        verify(paymentRepository).findById(paymentId);
        verify(paymentRepository).save(paymentEntity);
        assertThat(paymentEntity.isPaid()).isTrue();
    }

    @Test
    void updatePaymentStatus_shouldThrowNotFoundExceptionWhenPaymentDoesNotExist() {
        Long paymentId = 1L;
        boolean isPaid = true;

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updatePaymentStatusUseCase.updatePaymentStatus(paymentId, isPaid))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("400 BAD_REQUEST \"Payment not found_NOT_FOUND\"");

        verify(paymentRepository).findById(paymentId);
        verify(paymentRepository, never()).save(any());
    }
}