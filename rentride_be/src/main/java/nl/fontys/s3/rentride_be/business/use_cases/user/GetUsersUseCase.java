package nl.fontys.s3.rentride_be.business.use_cases.user;

import nl.fontys.s3.rentride_be.domain.user.User;

import java.util.List;

public interface GetUsersUseCase {
    List<User> getUsers();
}
