package nl.fontys.s3.rentride_be.business.impl.auth;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.use_cases.auth.LoginUseCase;
import nl.fontys.s3.rentride_be.configuration.security.token.AccessTokenEncoder;
import nl.fontys.s3.rentride_be.configuration.security.token.impl.AccessTokenImpl;
import nl.fontys.s3.rentride_be.domain.auth.LoginRequest;
import nl.fontys.s3.rentride_be.domain.auth.LoginResponse;
import nl.fontys.s3.rentride_be.persistance.UserRepository;
import nl.fontys.s3.rentride_be.persistance.entity.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LoginUseCaseImpl implements LoginUseCase {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AccessTokenEncoder accessTokenEncoder;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Optional<UserEntity> optionalUser = userRepository.findByEmail(loginRequest.getEmail());
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("Login->User");
        }
        UserEntity userEntity = optionalUser.get();

        if (!matchesPassword(loginRequest.getPassword(), userEntity.getPassword())) {
            throw new IllegalArgumentException("Invalid Credentials");
        }

        String accessToken = generateAccessToken(userEntity);
        return LoginResponse.builder().accessToken(accessToken).build();

    }

    private boolean matchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private String generateAccessToken(UserEntity user) {
        List<String> roles = user.getUserRoles().stream()
                .map(userRole -> userRole.getRole().toString())
                .toList();

        return accessTokenEncoder.encode(
                new AccessTokenImpl(user.getEmail(), user.getId(), roles));
    }
}
