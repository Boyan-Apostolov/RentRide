package nl.fontys.s3.rentride_be.business.impl.auth;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.AlreadyExistsException;
import nl.fontys.s3.rentride_be.business.use_cases.auth.RegisterUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.user.CreateUserUseCase;
import nl.fontys.s3.rentride_be.configuration.security.token.AccessTokenEncoder;
import nl.fontys.s3.rentride_be.domain.auth.RegisterRequest;
import nl.fontys.s3.rentride_be.domain.user.CreateUserRequest;
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
    private AccessTokenEncoder accessTokenEncoder;
    private CreateUserUseCase createUserUseCase;

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
    }
}
