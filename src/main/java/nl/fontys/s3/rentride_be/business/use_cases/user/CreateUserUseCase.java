package nl.fontys.s3.rentride_be.business.use_cases.user;

import nl.fontys.s3.rentride_be.domain.user.CreateUserRequest;
import nl.fontys.s3.rentride_be.domain.user.CreateUserResponse;

public interface CreateUserUseCase {
    CreateUserResponse createUser(CreateUserRequest request);
}
