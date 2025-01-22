package nl.fontys.s3.rentride_be.business.use_cases.user;

import nl.fontys.s3.rentride_be.domain.user.User;

public interface GetUserUseCase {
    User getUser(Long userId);

    User getSessionUser();
}
