package nl.fontys.s3.rentride_be.business.impl.auth;

import nl.fontys.s3.rentride_be.business.exception.AlreadyExistsException;
import nl.fontys.s3.rentride_be.business.use_cases.user.CreateUserUseCase;
import nl.fontys.s3.rentride_be.configuration.security.token.AccessTokenEncoder;
import nl.fontys.s3.rentride_be.domain.auth.RegisterRequest;
import nl.fontys.s3.rentride_be.domain.user.CreateUserRequest;
import nl.fontys.s3.rentride_be.persistance.UserRepository;
import nl.fontys.s3.rentride_be.persistance.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccessTokenEncoder accessTokenEncoder;

    @Mock
    private CreateUserUseCase createUserUseCase;

    @InjectMocks
    private RegisterUseCaseImpl registerUseCase;

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = RegisterRequest.builder()
                .email("test@example.com")
                .name("Test User")
                .birthDate(LocalDate.of(1990, 1, 1))
                .password("rawPassword")
                .build();
    }

    @Test
    void register_ShouldCreateUser_WhenEmailDoesNotExist() {
        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");

        registerUseCase.register(registerRequest);

        verify(userRepository, times(1)).findByEmail(registerRequest.getEmail());
        verify(passwordEncoder, times(1)).encode(registerRequest.getPassword());
        verify(createUserUseCase, times(1)).createUser(CreateUserRequest.builder()
                .email(registerRequest.getEmail())
                .name(registerRequest.getName())
                .birthDate(registerRequest.getBirthDate())
                .password("encodedPassword")
                .build());
    }

    @Test
    void register_ShouldThrowAlreadyExistsException_WhenEmailAlreadyExists() {
        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.of(new UserEntity()));

        assertThrows(AlreadyExistsException.class, () -> registerUseCase.register(registerRequest));

        verify(userRepository, times(1)).findByEmail(registerRequest.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
        verify(createUserUseCase, never()).createUser(any());
    }
}