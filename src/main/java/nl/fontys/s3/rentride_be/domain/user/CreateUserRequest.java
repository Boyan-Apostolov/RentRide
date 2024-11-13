package nl.fontys.s3.rentride_be.domain.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    @NotBlank
    private String name;
    @NotBlank

    private String email;

    @NotBlank
    private String password;

    @DateTimeFormat
    private LocalDate birthDate;
}
