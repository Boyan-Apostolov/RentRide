package nl.fontys.s3.rentride_be.business.useCases.user;

import nl.fontys.s3.rentride_be.domain.car.Car;
import nl.fontys.s3.rentride_be.domain.user.User;

public interface GetUserUseCase {
    User getUser(Long userId);
}
