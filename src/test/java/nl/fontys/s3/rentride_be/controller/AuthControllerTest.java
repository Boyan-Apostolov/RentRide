package nl.fontys.s3.rentride_be.controller;

import nl.fontys.s3.rentride_be.business.use_cases.auth.LoginUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.auth.RegisterUseCase;
import nl.fontys.s3.rentride_be.controller.AuthController;
import nl.fontys.s3.rentride_be.domain.auth.LoginRequest;
import nl.fontys.s3.rentride_be.domain.auth.LoginResponse;
import nl.fontys.s3.rentride_be.domain.auth.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private LoginUseCase loginUseCase;

    @Mock
    private RegisterUseCase registerUseCase;

    @InjectMocks
    private AuthController authController;

    private LoginRequest loginRequest;
    private LoginResponse loginResponse;

    @BeforeEach
    void setUp() {
        loginRequest = LoginRequest.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        loginResponse = LoginResponse.builder()
                .accessToken("mockAccessToken")
                .build();
    }

    @Test
    void login_ShouldReturnCreatedResponse_WhenCredentialsAreValid() {
        when(loginUseCase.login(loginRequest)).thenReturn(loginResponse);

        ResponseEntity<LoginResponse> response = authController.login(loginRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(loginResponse, response.getBody());
        verify(loginUseCase, times(1)).login(loginRequest);
    }
}