package nl.fontys.s3.rentride_be.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private LocalDate birthDate;
}
