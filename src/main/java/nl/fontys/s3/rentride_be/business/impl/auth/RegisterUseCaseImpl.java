package nl.fontys.s3.rentride_be.business.impl.auth;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.AlreadyExistsException;
import nl.fontys.s3.rentride_be.business.use_cases.auth.EmailerUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.auth.RegisterUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.user.CreateUserUseCase;
import nl.fontys.s3.rentride_be.domain.auth.RegisterRequest;
import nl.fontys.s3.rentride_be.domain.user.CreateUserRequest;
import nl.fontys.s3.rentride_be.domain.user.EmailType;
import nl.fontys.s3.rentride_be.persistance.UserRepository;
import nl.fontys.s3.rentride_be.persistance.entity.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RegisterUseCaseImpl implements RegisterUseCase {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private CreateUserUseCase createUserUseCase;
    private EmailerUseCase emailerUseCase;

    @Override
    public void register(RegisterRequest registerRequest) {
        Optional<UserEntity> optionalUser = userRepository.findByEmail(registerRequest.getEmail());
        if (optionalUser.isPresent()) {
            throw new AlreadyExistsException("Register->Email");
        }

        createUserUseCase.createUser(CreateUserRequest.builder()
                .email(registerRequest.getEmail())
                .name(registerRequest.getName())
                .birthDate(registerRequest.getBirthDate())
                        .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build());

        emailerUseCase.send(registerRequest.getEmail(),
                "Welcome to RentRide!", "Your registration was successful! Thank you for registering with RentRide, your partner in hassle-free and eco-friendly car rentals! We’re thrilled to have you onboard and can’t wait for you to explore our fleet of vehicles.",
                EmailType.PROMO);
    }
}
