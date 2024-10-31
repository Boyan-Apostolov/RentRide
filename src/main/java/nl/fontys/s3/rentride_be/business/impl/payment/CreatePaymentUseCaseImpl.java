package nl.fontys.s3.rentride_be.business.impl.payment;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.impl.user.UserConverter;
import nl.fontys.s3.rentride_be.business.use_cases.payment.CreatePaymentUseCase;
import nl.fontys.s3.rentride_be.domain.payment.CreatePaymentRequest;
import nl.fontys.s3.rentride_be.domain.payment.Payment;
import nl.fontys.s3.rentride_be.domain.user.User;
import nl.fontys.s3.rentride_be.persistance.BookingRepository;
import nl.fontys.s3.rentride_be.persistance.PaymentRepository;
import nl.fontys.s3.rentride_be.persistance.UserRepository;
import nl.fontys.s3.rentride_be.persistance.entity.PaymentEntity;
import nl.fontys.s3.rentride_be.persistance.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CreatePaymentUseCaseImpl implements CreatePaymentUseCase {
    private PaymentRepository paymentRepository;
    private BookingRepository bookingRepository;
    private UserRepository userRepository;

    @Override
    public PaymentEntity createPayment(CreatePaymentRequest createPaymentRequest) {
        if(!bookingRepository.existsById(createPaymentRequest.getBookingId())) throw new NotFoundException("CreatePayment->Booking");

        Optional<UserEntity> user = userRepository.findById(createPaymentRequest.getUserId());
        if (user.isEmpty()) throw new NotFoundException("CreatePayment->User");
        UserEntity foundUser = user.get();

        return paymentRepository.save(PaymentEntity.builder()
                .amount(createPaymentRequest.getTotalCost())
                .description(createPaymentRequest.getDescription())
                .stripeLink("")
                .user(foundUser)
                .createdOn(LocalDateTime.now())
                .build());
    }
}
