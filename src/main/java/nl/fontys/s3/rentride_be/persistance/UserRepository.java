package nl.fontys.s3.rentride_be.persistance;

import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
import nl.fontys.s3.rentride_be.persistance.entity.UserEntity;

import java.util.List;

public interface UserRepository {
    boolean existsByEmail(String email);

    boolean existsById(long userId);

    UserEntity findById(long userId);

    void deleteById(long userId);

    UserEntity save(UserEntity user);

    List<UserEntity> findAll();

    int count();
}
