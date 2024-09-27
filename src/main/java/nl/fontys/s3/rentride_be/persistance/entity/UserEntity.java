package nl.fontys.s3.rentride_be.persistance.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class UserEntity {
    private Long id;
    private String Name;
    private String Email;
    private String Password;
    private UserRole Role;
    private Date BirthDate;
    private String CustomerId;
}