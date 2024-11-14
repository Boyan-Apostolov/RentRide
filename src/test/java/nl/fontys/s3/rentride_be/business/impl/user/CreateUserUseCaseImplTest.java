package nl.fontys.s3.rentride_be.business.impl.user;

import nl.fontys.s3.rentride_be.business.exception.AlreadyExistsException;
import nl.fontys.s3.rentride_be.domain.user.CreateUserRequest;
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

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateUserUseCaseImpl createUserUseCase;

    private CreateUserRequest request;
    private UserEntity savedUser;

    @BeforeEach
    void setUp() {
        request = new CreateUserRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setName("John Doe");
        request.setBirthDate(LocalDate.of(1990, 1, 1));

        savedUser = UserEntity.builder()
                .id(1L)
                .email(request.getEmail())
                .password(request.getPassword())
                .name(request.getName())
                .birthDate(request.getBirthDate())
                .build();

        UserRoleEntity customerRole = UserRoleEntity.builder().role(UserRole.CUSTOMER).user(savedUser).build();
        savedUser.setUserRoles(Set.of(customerRole));
    }

    @Test
    void createUser_ShouldSaveUser_WhenEmailDoesNotExist() {
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUser);

        UserEntity result = createUserUseCase.createUser(request);

        assertEquals(savedUser.getId(), result.getId());
        assertEquals(savedUser.getEmail(), result.getEmail());
        assertEquals(savedUser.getName(), result.getName());
        assertEquals(savedUser.getBirthDate(), result.getBirthDate());
        assertEquals(1, result.getUserRoles().size());
        assertEquals(UserRole.CUSTOMER, result.getUserRoles().iterator().next().getRole());

        verify(userRepository, times(1)).existsByEmail(request.getEmail());
        verify(userRepository, times(1)).save(argThat(user ->
                user.getEmail().equals(request.getEmail()) &&
                        user.getPassword().equals(request.getPassword()) &&
                        user.getName().equals(request.getName()) &&
                        user.getBirthDate().equals(request.getBirthDate()) &&
                        user.getUserRoles().size() == 1 &&
                        user.getUserRoles().iterator().next().getRole() == UserRole.CUSTOMER
        ));
    }

    @Test
    void createUser_ShouldThrowAlreadyExistsException_WhenEmailAlreadyExists() {
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> createUserUseCase.createUser(request));

        verify(userRepository, times(1)).existsByEmail(request.getEmail());
        verify(userRepository, never()).save(any(UserEntity.class));
    }
}