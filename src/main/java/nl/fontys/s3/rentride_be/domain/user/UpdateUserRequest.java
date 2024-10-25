package nl.fontys.s3.rentride_be.domain.user;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDate;

@Data
@Builder
public class UpdateUserRequest {
    private Long id;

    @NotBlank
    private String name;
    @NotBlank

    private String email;

    @NotBlank
    private String password;

    @NumberFormat
    @Min(0)
    @Max(1)
    private Integer role;

    @DateTimeFormat
    private LocalDate birthDate;

}
