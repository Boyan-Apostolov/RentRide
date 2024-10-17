package nl.fontys.s3.rentride_be.persistance.impl;

import lombok.RequiredArgsConstructor;
import nl.fontys.s3.rentride_be.persistance.CarRepository;
import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
//
//@Repository
//@RequiredArgsConstructor
//public class FakeCarRepository implements CarRepository {
//    private static long NEXT_ID = 1;
//    private final List<CarEntity> savedCars;
//
//    @Override
//    public boolean existsByRegistrationNumber(String number) {
//        return this.savedCars
//                .stream()
//                .anyMatch(carEntity -> carEntity.getRegistrationNumber().equals(number));
//    }
//
//    @Override
//    public boolean existsById(long carId) {
//        return this.savedCars
//                .stream()
//                .anyMatch(carEntity -> carEntity.getId().equals(carId));
//    }
//
//    @Override
//    public CarEntity findById(long carId) {
//        return this.savedCars
//                .stream()
//                .filter(carEntity -> carEntity.getId().equals(carId))  // Use equals for comparison
//                .findFirst()
//                .orElse(null);
//    }
//
//    @Override
//    public void deleteById(long carId) {
//        this.savedCars.removeIf(carEntity -> carEntity.getId().equals(carId));
//    }
//
//    @Override
//    public CarEntity save(CarEntity car) {
//        if (car.getId() == null) {
//            car.setId(NEXT_ID);
//            NEXT_ID++;
//            this.savedCars.add(car);
//        }
//        return car;
//    }
//
//    @Override
//    public List<CarEntity> findAll() { return Collections.unmodifiableList(this.savedCars); }
//
//    @Override
//    public int count() { return this.savedCars.size(); }
//}
