package nl.fontys.s3.rentride_be.business.impl.user;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.user.GetUserUseCase;
import nl.fontys.s3.rentride_be.configuration.security.token.AccessToken;
import nl.fontys.s3.rentride_be.domain.user.User;
import nl.fontys.s3.rentride_be.persistance.UserRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetUserUseCaseImpl implements GetUserUseCase {
    private UserRepository userRepository;
    private AccessToken requestAccessToken;

    @Override
    public User getUser(Long carId) {
        return UserConverter.convert(this.userRepository.findById(carId).orElse(null));
    }

    @Override
    public User getSessionUser() {
        Long userId = requestAccessToken.getUserId();
        return getUser(userId);
    }
}
