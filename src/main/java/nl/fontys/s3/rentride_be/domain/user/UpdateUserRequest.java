package nl.fontys.s3.rentride_be.domain.user;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class UpdateUserRequest {
    private Long id;

    @NotBlank
    private String Name;
    @NotBlank

    private String Email;

    @NotBlank
    private String Password;

    @NumberFormat
    @Min(1)
    @Max(2)
    private Integer Role;

    @DateTimeFormat
    private Date BirthDate;

    private String CustomerId;
}
