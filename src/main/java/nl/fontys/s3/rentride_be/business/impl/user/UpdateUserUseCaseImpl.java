package nl.fontys.s3.rentride_be.business.impl.user;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.useCases.user.UpdateUserUseCase;
import nl.fontys.s3.rentride_be.domain.user.UserTransmissionType;
import nl.fontys.s3.rentride_be.domain.user.UpdateUserRequest;
import nl.fontys.s3.rentride_be.persistance.UserRepository;
import nl.fontys.s3.rentride_be.persistance.CityRepository;
import nl.fontys.s3.rentride_be.persistance.entity.UserEntity;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
import nl.fontys.s3.rentride_be.persistance.entity.UserRole;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdateUserUseCaseImpl implements UpdateUserUseCase {
    private UserRepository userRepository;

    @Override
    public void updateUser(UpdateUserRequest request) {
        verifyUserExists(request.getId());

        updateEntity(request);
    }

    private void verifyUserExists(Long userId){
        UserEntity userOptional = this.userRepository.findById(userId);
        if (userOptional == null) {
            throw new NotFoundException("Update->User");
        }
    }

    private void updateEntity(UpdateUserRequest request) {
        UserEntity userEntity = this.userRepository.findById(request.getId());

        userEntity.setEmail(request.getEmail());
        userEntity.setPassword(request.getPassword());
        userEntity.setName(request.getName());
        userEntity.setRole(UserRole.values()[request.getRole()]);
        userEntity.setBirthDate(request.getBirthDate());
        userEntity.setBirthDate(request.getBirthDate());
        userEntity.setCustomerId(request.getCustomerId());

        this.userRepository.save(userEntity);
    }
}
