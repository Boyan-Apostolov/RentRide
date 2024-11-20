package nl.fontys.s3.rentride_be.business.impl.user;

import nl.fontys.s3.rentride_be.domain.user.User;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetUserUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GetUserUseCaseImpl getUserUseCase;

    private UserEntity userEntity;
    private User user;

    @BeforeEach
    void setUp() {
        userEntity = UserEntity.builder()
                .id(1L)
                .email("test@example.com")
                .password("password")
                .name("Test User")
                .birthDate(LocalDate.of(1990, 1, 1))
                .build();

        user = UserConverter.convert(userEntity);
    }

    @Test
    void getUser_ShouldReturnUser_WhenUserExists() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        User result = getUserUseCase.getUser(userId);

        assertNotNull(result);
        assertEquals(user, result);

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getUser_ShouldReturnNull_WhenUserDoesNotExist() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        User result = getUserUseCase.getUser(userId);

        assertNull(result);

        verify(userRepository, times(1)).findById(userId);
    }
}