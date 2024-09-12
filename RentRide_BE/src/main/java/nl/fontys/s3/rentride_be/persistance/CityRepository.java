package nl.fontys.s3.rentride_be.persistance;

import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;

import java.util.List;

public interface CityRepository {
    boolean existsByName(String name);

    boolean existsById(long cityId);

    CityEntity findById(long cityId);

    void deleteById(long cityId);

    CityEntity save(CityEntity city);

    List<CityEntity> findAll();

    int count();
}
