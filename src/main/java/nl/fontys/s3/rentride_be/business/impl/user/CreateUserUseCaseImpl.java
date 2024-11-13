package nl.fontys.s3.rentride_be.business.impl.user;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.AlreadyExistsException;
import nl.fontys.s3.rentride_be.business.use_cases.user.CreateUserUseCase;
import nl.fontys.s3.rentride_be.domain.user.CreateUserRequest;
import nl.fontys.s3.rentride_be.persistance.UserRepository;
import nl.fontys.s3.rentride_be.persistance.entity.UserEntity;
import nl.fontys.s3.rentride_be.persistance.entity.UserRole;
import nl.fontys.s3.rentride_be.persistance.entity.UserRoleEntity;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class CreateUserUseCaseImpl implements CreateUserUseCase {
    private UserRepository userRepository;

    @Override
    public UserEntity createUser(CreateUserRequest request) {
        if (this.userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("User");
        }

        return saveNewUser(request);
    }

    private UserEntity saveNewUser(CreateUserRequest request) {
        UserEntity userEntity = UserEntity.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .name(request.getName())
                .birthDate(request.getBirthDate())
                .build();

        UserRoleEntity customerRole = UserRoleEntity.builder().role(UserRole.CUSTOMER).user(userEntity).build();
        userEntity.setUserRoles(Set.of(customerRole));

        return this.userRepository.save(userEntity);
    }
}
