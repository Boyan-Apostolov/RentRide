package nl.fontys.s3.rentride_be.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import nl.fontys.s3.rentride_be.persistance.entity.UserRole;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private UserRole role;
    private LocalDate birthDate;
}
