package nl.fontys.s3.rentride_be.business.impl.user;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.use_cases.user.UpdateUserEmailSettingsUseCase;
import nl.fontys.s3.rentride_be.configuration.security.token.AccessToken;
import nl.fontys.s3.rentride_be.domain.user.UpdateUserEmailSettingsRequest;
import nl.fontys.s3.rentride_be.persistance.UserRepository;
import nl.fontys.s3.rentride_be.persistance.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateUserEmailSettingsUseCaseImpl implements UpdateUserEmailSettingsUseCase {
    private final UserRepository userRepository;
    private AccessToken requestAccessToken;
    @Override
    public void updateUserEmails(UpdateUserEmailSettingsRequest request) {
        Long currentUserId = requestAccessToken.getUserId();
        Optional<UserEntity> userOptional = userRepository.findById(currentUserId);

        if (userOptional.isEmpty()) throw new NotFoundException("UpdateUser -> User");

        UserEntity userEntity = userOptional.get();

        userEntity.setBookingsEmails(request.isBookingsEmails());
        userEntity.setDamageEmails(request.isDamageEmails());
        userEntity.setPromoEmails(request.isPromoEmails());

        userRepository.save(userEntity);
    }
}
