package nl.fontys.s3.rentride_be.business.impl.user;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.use_cases.user.UpdateUserUseCase;
import nl.fontys.s3.rentride_be.domain.user.UpdateUserRequest;
import nl.fontys.s3.rentride_be.persistance.UserRepository;
import nl.fontys.s3.rentride_be.persistance.entity.UserEntity;
import nl.fontys.s3.rentride_be.persistance.entity.UserRole;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateUserUseCaseImpl implements UpdateUserUseCase {
    private UserRepository userRepository;

    @Override
    public void updateUser(UpdateUserRequest request) {
        updateEntity(request);
    }

    private void updateEntity(UpdateUserRequest request) {
        Optional<UserEntity> userEntityOptional = this.userRepository.findById(request.getId());

        if (userEntityOptional.isEmpty()) {
            throw new NotFoundException("Update->User");
        }

        UserEntity userEntity = userEntityOptional.get();
        userEntity.setEmail(request.getEmail());
        userEntity.setPassword(request.getPassword());
        userEntity.setName(request.getName());
        userEntity.setRole(UserRole.values()[request.getRole()]);
        userEntity.setBirthDate(request.getBirthDate());
        userEntity.setBirthDate(request.getBirthDate());

        this.userRepository.save(userEntity);
    }
}
