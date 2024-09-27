package nl.fontys.s3.rentride_be.business.impl.user;

import nl.fontys.s3.rentride_be.business.impl.city.CityConverter;
import nl.fontys.s3.rentride_be.domain.car.Car;
import nl.fontys.s3.rentride_be.domain.user.User;
import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
import nl.fontys.s3.rentride_be.persistance.entity.UserEntity;

public final class UserConverter {
    private UserConverter() {}

    public static User convert(UserEntity userEntity){
        if(userEntity == null) return null;
        return User.builder()
                .id(userEntity.getId())
                .Email(userEntity.getEmail())
                .Password(userEntity.getPassword())
                .Role(userEntity.getRole())
                .Name(userEntity.getName())
                .BirthDate(userEntity.getBirthDate())
                .CustomerId(userEntity.getCustomerId())
                .build();
    }
}
