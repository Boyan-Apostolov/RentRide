package nl.fontys.s3.rentride_be.business.impl.user;

import nl.fontys.s3.rentride_be.configuration.security.token.AccessToken;
import nl.fontys.s3.rentride_be.domain.user.User;
import nl.fontys.s3.rentride_be.persistance.UserRepository;
import nl.fontys.s3.rentride_be.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetUserUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccessToken accessToken;

    @InjectMocks
    private GetUserUseCaseImpl getUserUseCase;

    @Test
    void getUser_shouldReturnUserWhenExists() {
        Long userId = 1L;

        UserEntity userEntity = UserEntity.builder()
                .id(userId)
                .name("John Doe")
                .email("john.doe@example.com")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        User result = getUserUseCase.getUser(userId);

        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getName()).isEqualTo("John Doe");
        assertThat(result.getEmail()).isEqualTo("john.doe@example.com");
        verify(userRepository).findById(userId);
    }

    @Test
    void getUser_shouldReturnNullWhenUserDoesNotExist() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        User result = getUserUseCase.getUser(userId);

        assertThat(result).isNull();
        verify(userRepository).findById(userId);
    }

    @Test
    void getSessionUser_shouldReturnSessionUser() {
        Long sessionUserId = 1L;

        UserEntity userEntity = UserEntity.builder()
                .id(sessionUserId)
                .name("Session User")
                .email("session.user@example.com")
                .build();

        when(accessToken.getUserId()).thenReturn(sessionUserId);
        when(userRepository.findById(sessionUserId)).thenReturn(Optional.of(userEntity));

        User result = getUserUseCase.getSessionUser();

        assertThat(result.getId()).isEqualTo(sessionUserId);
        assertThat(result.getName()).isEqualTo("Session User");
        assertThat(result.getEmail()).isEqualTo("session.user@example.com");
        verify(accessToken).getUserId();
        verify(userRepository).findById(sessionUserId);
    }
}