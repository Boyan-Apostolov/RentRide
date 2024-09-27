package nl.fontys.s3.rentride_be.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import nl.fontys.s3.rentride_be.persistance.entity.UserRole;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class User {
    private Long id;
    private String Name;
    private String Email;
    private String Password;
    private UserRole Role;
    private Date BirthDate;
    private String CustomerId;
}
