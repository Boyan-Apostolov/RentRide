package nl.fontys.s3.rentride_be.business.use_cases.auth;

import nl.fontys.s3.rentride_be.domain.auth.LoginOAuthRequest;
import nl.fontys.s3.rentride_be.domain.auth.LoginRequest;
import nl.fontys.s3.rentride_be.domain.auth.LoginResponse;

public interface LoginUseCase {
    LoginResponse login(LoginRequest loginRequest);

    LoginResponse login(LoginOAuthRequest loginRequest);
}
