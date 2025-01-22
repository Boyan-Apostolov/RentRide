package nl.fontys.s3.rentride_be.business.impl.user;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.InvalidAccessTokenException;
import nl.fontys.s3.rentride_be.business.exception.InvalidOperationException;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.use_cases.user.UpdateUserUseCase;
import nl.fontys.s3.rentride_be.configuration.security.token.AccessToken;
import nl.fontys.s3.rentride_be.domain.auth.GoogleOAuthRequest;
import nl.fontys.s3.rentride_be.domain.user.UpdateUserRequest;
import nl.fontys.s3.rentride_be.persistance.UserRepository;
import nl.fontys.s3.rentride_be.persistance.entity.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateUserUseCaseImpl implements UpdateUserUseCase {
    private UserRepository userRepository;
    private AccessToken accessToken;
    private PasswordEncoder passwordEncoder;

    @Override
    public void updateUser(UpdateUserRequest request) {
        updateEntity(request);
    }

    @Override
    public void updateUser(GoogleOAuthRequest request) {
        UserEntity userEntity = tryGetUser(request.getId());

        userEntity.setGoogleOAuthId(request.getOAuthId());

        userRepository.save(userEntity);
    }

    private void updateEntity(UpdateUserRequest request) {
        UserEntity userEntity = tryGetUser(request.getId());

        if (!request.getCurrentPassword().isEmpty()) {
            if (!passwordEncoder.matches(request.getCurrentPassword(), userEntity.getPassword())) {
                throw new InvalidOperationException("Current Password does not match");
            }else {
                userEntity.setPassword(passwordEncoder.encode(request.getNewPassword()));
            }
        }

        Optional<UserEntity> optionalUserByEmail = userRepository.findByEmail(request.getEmail());
        if (optionalUserByEmail.isEmpty() || Objects.equals(optionalUserByEmail.get().getId(), userEntity.getId())) {
            userEntity.setEmail(request.getEmail());
        } else {
            throw new InvalidOperationException("Email already exists");
        }

        userEntity.setName(request.getName());
        userEntity.setBirthDate(request.getBirthDate());

        this.userRepository.save(userEntity);
    }

    private UserEntity tryGetUser(Long request) {
        if (!Objects.equals(request, accessToken.getUserId()))
            throw new InvalidAccessTokenException("Access denied");

        Optional<UserEntity> userEntityOptional = this.userRepository.findById(request);

        if (userEntityOptional.isEmpty()) {
            throw new NotFoundException("Update->User");
        }

        return userEntityOptional.get();
    }
}