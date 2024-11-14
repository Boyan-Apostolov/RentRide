package nl.fontys.s3.rentride_be.business.impl.payment;

import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.domain.payment.CreatePaymentRequest;
import nl.fontys.s3.rentride_be.persistance.PaymentRepository;
import nl.fontys.s3.rentride_be.persistance.UserRepository;
import nl.fontys.s3.rentride_be.persistance.entity.PaymentEntity;
import nl.fontys.s3.rentride_be.persistance.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreatePaymentUseCaseImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreatePaymentUseCaseImpl createPaymentUseCase;

    private CreatePaymentRequest request;
    private UserEntity mockUser;
    private PaymentEntity savedPayment;

    @BeforeEach
    void setUp() {
        request = new CreatePaymentRequest();
        request.setUserId(1L);
        request.setTotalCost(150.0);
        request.setDescription("Rental Payment");

        mockUser = new UserEntity();
        mockUser.setId(1L);

        savedPayment = PaymentEntity.builder()
                .id(1L)
                .amount(request.getTotalCost())
                .description(request.getDescription())
                .stripeLink("")
                .user(mockUser)
                .createdOn(LocalDateTime.now())
                .build();
    }

    @Test
    void createPayment_ShouldReturnPaymentEntity_WhenUserExists() {
        when(userRepository.findById(request.getUserId())).thenReturn(Optional.of(mockUser));
        when(paymentRepository.save(any(PaymentEntity.class))).thenReturn(savedPayment);

        PaymentEntity result = createPaymentUseCase.createPayment(request);

        assertEquals(savedPayment.getId(), result.getId());
        assertEquals(savedPayment.getAmount(), result.getAmount());
        assertEquals(savedPayment.getDescription(), result.getDescription());
        assertEquals(savedPayment.getUser(), result.getUser());

        verify(userRepository, times(1)).findById(request.getUserId());
        verify(paymentRepository, times(1)).save(argThat(payment ->
                payment.getAmount() == request.getTotalCost() &&
                        payment.getDescription().equals(request.getDescription()) &&
                        payment.getUser().equals(mockUser)
        ));
    }

    @Test
    void createPayment_ShouldThrowNotFoundException_WhenUserNotFound() {
        when(userRepository.findById(request.getUserId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> createPaymentUseCase.createPayment(request));

        verify(userRepository, times(1)).findById(request.getUserId());
        verify(paymentRepository, never()).save(any(PaymentEntity.class));
    }
}