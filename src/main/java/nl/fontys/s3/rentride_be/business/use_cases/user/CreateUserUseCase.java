package nl.fontys.s3.rentride_be.business.use_cases.user;

import nl.fontys.s3.rentride_be.domain.user.CreateUserRequest;
import nl.fontys.s3.rentride_be.domain.user.CreateUserResponse;
import nl.fontys.s3.rentride_be.persistance.entity.UserEntity;

public interface CreateUserUseCase {
    UserEntity createUser(CreateUserRequest request);
}
