package nl.fontys.s3.rentride_be.business.impl.user;

import nl.fontys.s3.rentride_be.business.exception.InvalidAccessTokenException;
import nl.fontys.s3.rentride_be.business.exception.InvalidOperationException;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.configuration.security.token.AccessToken;
import nl.fontys.s3.rentride_be.domain.user.UpdateUserRequest;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserUseCaseImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private AccessToken accessToken;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UpdateUserUseCaseImpl updateUserUseCase;

    private UserEntity existingUser;

    @BeforeEach
    void setup() {
        existingUser = UserEntity.builder()
                .id(1L)
                .email("old.email@example.com")
                .name("Old Name")
                .password("encodedPassword")
                .birthDate(LocalDate.of(1990, 1, 1))
                .build();
    }

    @Test
    void updateUser_shouldThrowInvalidAccessTokenExceptionWhenAccessDenied() {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .id(2L) // Different ID to simulate access denial
                .build();

        when(accessToken.getUserId()).thenReturn(1L);
        when(accessToken.hasRole("ADMIN")).thenReturn(false);

        assertThatThrownBy(() -> updateUserUseCase.updateUser(request))
                .isInstanceOf(InvalidAccessTokenException.class)
                .hasMessage("401 UNAUTHORIZED \"Access denied\"");

        verify(accessToken).getUserId();
        verify(accessToken).hasRole("ADMIN");
        verifyNoInteractions(userRepository);
    }

    @Test
    void updateUser_shouldThrowNotFoundExceptionWhenUserNotFound() {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .id(1L)
                .build();

        when(accessToken.getUserId()).thenReturn(1L);
        when(accessToken.hasRole("ADMIN")).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updateUserUseCase.updateUser(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("400 BAD_REQUEST \"Update->User_NOT_FOUND\"");

        verify(userRepository).findById(1L);
    }

    @Test
    void updateUser_shouldThrowInvalidOperationExceptionWhenPasswordDoesNotMatch() {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .id(1L)
                .currentPassword("wrongPassword")
                .newPassword("newPassword")
                .build();

        when(accessToken.getUserId()).thenReturn(1L);
        when(accessToken.hasRole("ADMIN")).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        assertThatThrownBy(() -> updateUserUseCase.updateUser(request))
                .isInstanceOf(InvalidOperationException.class)
                .hasMessage("400 BAD_REQUEST \"Current Password does not match\"");

        verify(passwordEncoder).matches("wrongPassword", "encodedPassword");
    }

    @Test
    void updateUser_shouldUpdateUserSuccessfully() {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .id(1L)
                .email("new.email@example.com")
                .name("New Name")
                .birthDate(LocalDate.of(1995, 5, 5))
                .currentPassword("currentPassword")
                .newPassword("newPassword")
                .build();

        when(accessToken.getUserId()).thenReturn(1L);
        when(accessToken.hasRole("ADMIN")).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("currentPassword", "encodedPassword")).thenReturn(true);
        when(userRepository.findByEmail("new.email@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("newPassword")).thenReturn("newEncodedPassword");

        updateUserUseCase.updateUser(request);

        verify(userRepository).save(argThat(user ->
                user.getEmail().equals("new.email@example.com") &&
                        user.getName().equals("New Name") &&
                        user.getBirthDate().equals(LocalDate.of(1995, 5, 5)) &&
                        user.getPassword().equals("newEncodedPassword")
        ));
    }
}