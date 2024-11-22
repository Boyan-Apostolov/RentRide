package nl.fontys.s3.rentride_be.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.fontys.s3.rentride_be.business.use_cases.auth.LoginUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.auth.RegisterUseCase;
import nl.fontys.s3.rentride_be.domain.auth.LoginRequest;
import nl.fontys.s3.rentride_be.domain.auth.LoginResponse;
import nl.fontys.s3.rentride_be.domain.auth.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private LoginUseCase loginUseCase;

    @Mock
    private RegisterUseCase registerUseCase;

    @InjectMocks
    private AuthController authController;

    @Test
    void login_shouldReturn400_whenRequestIsInvalid() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("invalid-email")
                .password("")
                .build();

        String expectedErrorResponse = """
                {
                    "errors": [
                        {"field": "email", "error": "must be a well-formed email address"},
                        {"field": "password", "error": "must not be blank"}
                    ]
                }
                """;

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedErrorResponse, false));

        verifyNoInteractions(loginUseCase);
    }


    @Test
    void register_shouldReturn400_whenRequestIsInvalid() throws Exception {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .name("")
                .email("invalid-email")
                .password("")
                .birthDate(null)
                .build();

        String expectedErrorResponse = """
                {
                    "errors": [
                        {"field": "name", "error": "size must be between 3 and 255"},
                        {"field": "email", "error": "must be a well-formed email address"},
                        {"field": "password", "error": "size must be between 3 and 255"}
                    ]
                }
                """;

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedErrorResponse, false));

        verifyNoInteractions(registerUseCase);
        verifyNoInteractions(loginUseCase);
    }
}