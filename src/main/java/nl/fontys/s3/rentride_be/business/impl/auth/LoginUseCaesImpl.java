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

@Service
@AllArgsConstructor
public class LoginUseCaesImpl implements LoginUseCase {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AccessTokenEncoder accessTokenEncoder;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        UserEntity user = userRepository.findByEmail(loginRequest.getUsername());
        if (user == null) {
            throw new NotFoundException("Login->User");
        }

        if (!matchesPassword(loginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid Credentials");
        }

        String accessToken = generateAccessToken(user);
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
