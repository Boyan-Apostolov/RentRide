package nl.fontys.s3.rentride_be.domain.user;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
import nl.fontys.s3.rentride_be.persistance.entity.UserRole;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

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

    @NumberFormat
    @Min(0)
    @Max(1)
    private Integer role;

    @DateTimeFormat
    private LocalDate birthDate;
}
