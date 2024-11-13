package nl.fontys.s3.rentride_be.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.auth.LoginUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.auth.RegisterUseCase;
import nl.fontys.s3.rentride_be.domain.auth.LoginRequest;
import nl.fontys.s3.rentride_be.domain.auth.LoginResponse;
import nl.fontys.s3.rentride_be.domain.auth.RegisterRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final RegisterUseCase registerUseCase;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse loginResponse = loginUseCase.login(loginRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(loginResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody @Valid RegisterRequest registerRequest) {
        registerUseCase.register(registerRequest);
        return login(LoginRequest.builder()
                .email(registerRequest.getEmail())
                .password(registerRequest.getPassword())
                .build());
    }
}
