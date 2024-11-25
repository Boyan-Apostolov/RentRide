package nl.fontys.s3.rentride_be.business.impl.user;

import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.configuration.security.token.AccessToken;
import nl.fontys.s3.rentride_be.domain.user.UpdateUserEmailSettingsRequest;
import nl.fontys.s3.rentride_be.persistance.UserRepository;
import nl.fontys.s3.rentride_be.persistance.entity.UserEntity;
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
class UpdateUserEmailSettingsUseCaseImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private AccessToken accessToken;

    @InjectMocks
    private UpdateUserEmailSettingsUseCaseImpl updateUserEmailSettingsUseCase;

    @Test
    void updateUserEmails_shouldUpdateEmailSettingsForCurrentUser() {
        long userId = 1L;
        UpdateUserEmailSettingsRequest request = UpdateUserEmailSettingsRequest.builder()
                .bookingsEmails(true)
                .damageEmails(false)
                .promoEmails(true)
                .build();

        UserEntity existingUser = UserEntity.builder()
                .id(userId)
                .bookingsEmails(false)
                .damageEmails(true)
                .promoEmails(false)
                .build();

        when(accessToken.getUserId()).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        updateUserEmailSettingsUseCase.updateUserEmails(request);

        assertThat(existingUser.isBookingsEmails()).isTrue();
        assertThat(existingUser.isDamageEmails()).isFalse();
        assertThat(existingUser.isPromoEmails()).isTrue();

        verify(userRepository).findById(userId);
        verify(userRepository).save(existingUser);
    }

    @Test
    void updateUserEmails_shouldThrowNotFoundExceptionWhenUserNotFound() {
        long userId = 1L;
        UpdateUserEmailSettingsRequest request = UpdateUserEmailSettingsRequest.builder()
                .bookingsEmails(true)
                .damageEmails(false)
                .promoEmails(true)
                .build();

        when(accessToken.getUserId()).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updateUserEmailSettingsUseCase.updateUserEmails(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("UpdateUser -> User");

        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any());
    }
}