package nl.fontys.s3.rentride_be.business.impl.user;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.AlreadyExistsException;
import nl.fontys.s3.rentride_be.business.use_cases.user.CreateUserUseCase;
import nl.fontys.s3.rentride_be.domain.user.CreateUserRequest;
import nl.fontys.s3.rentride_be.domain.user.CreateUserResponse;
import nl.fontys.s3.rentride_be.persistance.UserRepository;
import nl.fontys.s3.rentride_be.persistance.entity.UserEntity;
import nl.fontys.s3.rentride_be.persistance.entity.UserRole;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateUserUseCaseImpl implements CreateUserUseCase {
    private UserRepository userRepository;

    @Override
    public CreateUserResponse createUser(CreateUserRequest request) {
        if (this.userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("User");
        }

        //TODO: Set customer id from stripe

        UserEntity savedUser = saveNewUser(request);

        return CreateUserResponse.builder()
                .userId(savedUser.getId())
                .build();
    }

    private UserEntity saveNewUser(CreateUserRequest request) {
        UserEntity userEntity = UserEntity.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .role(UserRole.values()[request.getRole()])
                .name(request.getName())
                .birthDate(request.getBirthDate())
                .build();

        return this.userRepository.save(userEntity);
    }
}
