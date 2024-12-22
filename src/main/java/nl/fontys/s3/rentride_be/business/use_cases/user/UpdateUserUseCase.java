package nl.fontys.s3.rentride_be.business.use_cases.user;

import nl.fontys.s3.rentride_be.domain.auth.GoogleOAuthRequest;
import nl.fontys.s3.rentride_be.domain.user.UpdateUserRequest;

public interface UpdateUserUseCase {
    void updateUser(UpdateUserRequest request);

    void updateUser(GoogleOAuthRequest request);
}
