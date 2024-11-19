package nl.fontys.s3.rentride_be.business.impl.user;

import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.domain.user.UpdateUserRequest;
import nl.fontys.s3.rentride_be.persistance.UserRepository;
import nl.fontys.s3.rentride_be.persistance.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UpdateUserUseCaseImpl updateUserUseCase;

    private UpdateUserRequest updateUserRequest;

    @BeforeEach
    void setUp() {
        updateUserRequest = UpdateUserRequest.builder()
                .id(1L)
                .email("updated@example.com")
                .password("updatedPassword")
                .name("Updated User")
                .birthDate(LocalDate.of(1995, 5, 15))
                .build();
    }

    @Test
    void updateUser_ShouldUpdateSuccessfully_WhenUserExists() {
        UserEntity existingUser = UserEntity.builder()
                .id(1L)
                .email("old@example.com")
                .password("oldPassword")
                .name("Old User")
                .birthDate(LocalDate.of(1990, 1, 1))
                .build();

        when(userRepository.findById(updateUserRequest.getId())).thenReturn(Optional.of(existingUser));

        updateUserUseCase.updateUser(updateUserRequest);

        verify(userRepository, times(1)).findById(updateUserRequest.getId());
        verify(userRepository, times(1)).save(existingUser);

        assertEquals(updateUserRequest.getEmail(), existingUser.getEmail());
        assertEquals(updateUserRequest.getPassword(), existingUser.getPassword());
        assertEquals(updateUserRequest.getName(), existingUser.getName());
        assertEquals(updateUserRequest.getBirthDate(), existingUser.getBirthDate());
    }

    @Test
    void updateUser_ShouldThrowNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findById(updateUserRequest.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> updateUserUseCase.updateUser(updateUserRequest));

        verify(userRepository, times(1)).findById(updateUserRequest.getId());
        verify(userRepository, never()).save(any());
    }
}