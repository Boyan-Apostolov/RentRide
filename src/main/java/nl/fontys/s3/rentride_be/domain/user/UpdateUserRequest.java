package nl.fontys.s3.rentride_be.domain.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
public class UpdateUserRequest {
    private Long id;

    @NotBlank
    private String name;
    @NotBlank

    private String email;

    private String currentPassword;

    private String newPassword;

    @DateTimeFormat
    private LocalDate birthDate;

}
