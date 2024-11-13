package nl.fontys.s3.rentride_be.business.use_cases.auth;

import nl.fontys.s3.rentride_be.domain.auth.LoginResponse;
import nl.fontys.s3.rentride_be.domain.auth.RegisterRequest;

public interface RegisterUseCase {
    void register(RegisterRequest registerRequest);
}
