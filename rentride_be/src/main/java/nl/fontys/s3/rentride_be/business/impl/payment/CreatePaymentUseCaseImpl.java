package nl.fontys.s3.rentride_be.business.impl.payment;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.use_cases.auth.EmailerUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.payment.CreatePaymentUseCase;
import nl.fontys.s3.rentride_be.domain.payment.CreatePaymentRequest;
import nl.fontys.s3.rentride_be.domain.user.EmailType;
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
    private UserRepository userRepository;
    private EmailerUseCase emailerUseCase;

    @Override
    public PaymentEntity createPayment(CreatePaymentRequest createPaymentRequest) {

        Optional<UserEntity> user = userRepository.findById(createPaymentRequest.getUserId());
        if (user.isEmpty()) throw new NotFoundException("CreatePayment->User");
        UserEntity foundUser = user.get();

        PaymentEntity
                response = paymentRepository.save(PaymentEntity.builder()
                .amount(createPaymentRequest.getTotalCost())
                .description(createPaymentRequest.getDescription())
                .stripeLink("")
                .user(foundUser)
                .createdOn(LocalDateTime.now())
                .build());

        if (createPaymentRequest.getDescription().contains("Found Damage"))
            emailerUseCase.send(foundUser.getEmail(), "New payment request",
                    String.format("A new payment request with the description \" %s \" and a value of €%.2f has been assigned to you and needs to be paid. Please pay the request by going to My Profile -> Payments -> Pay.",
                            createPaymentRequest.getDescription(), createPaymentRequest.getTotalCost()),
                    EmailType.DAMAGE);

        return response;
    }
}
