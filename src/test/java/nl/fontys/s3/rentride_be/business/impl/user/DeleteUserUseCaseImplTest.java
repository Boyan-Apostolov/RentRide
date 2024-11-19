package nl.fontys.s3.rentride_be.business.impl.user;

import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.impl.user.DeleteUserUseCaseImpl;
import nl.fontys.s3.rentride_be.persistance.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteUserUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DeleteUserUseCaseImpl deleteUserUseCase;

    @Test
    void deleteUser_ShouldDeleteSuccessfully_WhenUserExists() {
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(true);

        deleteUserUseCase.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void deleteUser_ShouldThrowNotFoundException_WhenUserDoesNotExist() {
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(NotFoundException.class, () ->
                deleteUserUseCase.deleteUser(userId));

        verify(userRepository, never()).deleteById(any());
    }
}