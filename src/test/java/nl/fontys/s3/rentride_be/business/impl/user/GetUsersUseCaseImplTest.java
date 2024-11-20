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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetUsersUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GetUsersUseCaseImpl getUsersUseCase;

    private UserEntity userEntity1;
    private UserEntity userEntity2;

    @BeforeEach
    void setUp() {
        userEntity1 = UserEntity.builder()
                .id(1L)
                .email("user1@example.com")
                .name("User One")
                .build();

        userEntity2 = UserEntity.builder()
                .id(2L)
                .email("user2@example.com")
                .name("User Two")
                .build();
    }

    @Test
    void getUsers_ShouldReturnAllUsers_WhenUsersExist() {
        when(userRepository.findAll()).thenReturn(List.of(userEntity1, userEntity2));

        List<User> result = getUsersUseCase.getUsers();

        assertEquals(2, result.size());
        assertEquals(userEntity1.getId(), result.get(0).getId());
        assertEquals(userEntity1.getEmail(), result.get(0).getEmail());
        assertEquals(userEntity2.getId(), result.get(1).getId());
        assertEquals(userEntity2.getEmail(), result.get(1).getEmail());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUsers_ShouldReturnEmptyList_WhenNoUsersExist() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<User> result = getUsersUseCase.getUsers();

        assertEquals(0, result.size());

        verify(userRepository, times(1)).findAll();
    }
}