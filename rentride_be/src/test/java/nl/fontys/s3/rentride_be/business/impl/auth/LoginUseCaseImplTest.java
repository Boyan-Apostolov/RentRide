package nl.fontys.s3.rentride_be.business.impl.auth;

import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.configuration.security.token.AccessTokenEncoder;
import nl.fontys.s3.rentride_be.configuration.security.token.impl.AccessTokenImpl;
import nl.fontys.s3.rentride_be.domain.auth.LoginRequest;
import nl.fontys.s3.rentride_be.domain.auth.LoginResponse;
import nl.fontys.s3.rentride_be.persistance.UserRepository;
import nl.fontys.s3.rentride_be.persistance.entity.UserEntity;
import nl.fontys.s3.rentride_be.persistance.entity.UserRole;
import nl.fontys.s3.rentride_be.persistance.entity.UserRoleEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccessTokenEncoder accessTokenEncoder;

    @InjectMocks
    private LoginUseCaseImpl loginUseCase;

    private UserEntity userEntity;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        userEntity = UserEntity.builder()
                .id(1L)
                .email("test@example.com")
                .password("encodedPassword")
                .userRoles(Set.of(
                        UserRoleEntity.builder().role(UserRole.CUSTOMER).build()
                ))
                .build();

        loginRequest = LoginRequest.builder()
                .email("test@example.com")
                .password("rawPassword")
                .build();
    }

    @Test
    void login_ShouldReturnAccessToken_WhenCredentialsAreValid() {
        String expectedAccessToken = "encodedAccessToken";

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword())).thenReturn(true);
        when(accessTokenEncoder.encode(any(AccessTokenImpl.class))).thenReturn(expectedAccessToken);

        LoginResponse response = loginUseCase.login(loginRequest);

        assertEquals(expectedAccessToken, response.getAccessToken());
        verify(userRepository, times(1)).findByEmail(loginRequest.getEmail());
        verify(passwordEncoder, times(1)).matches(loginRequest.getPassword(), userEntity.getPassword());
        verify(accessTokenEncoder, times(1)).encode(any(AccessTokenImpl.class));
    }

    @Test
    void login_ShouldThrowNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> loginUseCase.login(loginRequest));

        verify(userRepository, times(1)).findByEmail(loginRequest.getEmail());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(accessTokenEncoder, never()).encode(any());
    }

    @Test
    void login_ShouldThrowNotFoundException_WhenPasswordIsInvalid() {
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> loginUseCase.login(loginRequest));

        verify(userRepository, times(1)).findByEmail(loginRequest.getEmail());
        verify(passwordEncoder, times(1)).matches(loginRequest.getPassword(), userEntity.getPassword());
        verify(accessTokenEncoder, never()).encode(any());
    }
}