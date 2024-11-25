package nl.fontys.s3.rentride_be.business.impl.user;

import nl.fontys.s3.rentride_be.domain.user.User;
import nl.fontys.s3.rentride_be.persistance.entity.UserEntity;

public final class UserConverter {
    private UserConverter() {}

    public static User convert(UserEntity userEntity){
        if(userEntity == null) return null;
        return User.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .name(userEntity.getName())
                .birthDate(userEntity.getBirthDate())
                .bookingsEmails(userEntity.isBookingsEmails())
                .damageEmails(userEntity.isDamageEmails())
                .promoEmails(userEntity.isPromoEmails())
                .build();
    }
}
