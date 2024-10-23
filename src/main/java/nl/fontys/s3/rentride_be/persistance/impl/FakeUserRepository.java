//package nl.fontys.s3.rentride_be.persistance.impl;
//
//import lombok.RequiredArgsConstructor;
//import nl.fontys.s3.rentride_be.persistance.UserRepository;
//import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
//import nl.fontys.s3.rentride_be.persistance.entity.UserEntity;
//import org.springframework.stereotype.Repository;
//
//import java.util.Collections;
//import java.util.List;
//
//@Repository
//@RequiredArgsConstructor
//public class FakeUserRepository implements UserRepository {
//    private static long NEXT_ID = 1;
//
//    private final List<UserEntity> savedUsers;
//
//    @Override
//    public boolean existsByEmail(String email) {
//        return this.savedUsers
//                .stream()
//                .anyMatch(userEntity -> userEntity.getEmail().equals(email));
//    }
//
//    @Override
//    public boolean existsById(long userId) {
//        return this.savedUsers
//                .stream()
//                .anyMatch(userEntity -> userEntity.getId().equals(userId));
//    }
//
//    @Override
//    public UserEntity findById(long userId) {
//        return this.savedUsers
//                .stream()
//                .filter(userEntity -> userEntity.getId().equals(userId))  // Use equals for comparison
//                .findFirst()
//                .orElse(null);
//    }
//
//    @Override
//    public void deleteById(long userId) {
//        this.savedUsers.removeIf(userEntity -> userEntity.getId() == userId);
//    }
//
//    @Override
//    public UserEntity save(UserEntity user) {
//        if (user.getId() == null) {
//            user.setId(NEXT_ID);
//            NEXT_ID++;
//            this.savedUsers.add(user);
//        }
//        return user;
//    }
//
//    @Override
//    public List<UserEntity> findAll() {
//        return Collections.unmodifiableList(this.savedUsers);
//    }
//
//    @Override
//    public int count() {
//        return this.savedUsers.size();
//    }
//}
