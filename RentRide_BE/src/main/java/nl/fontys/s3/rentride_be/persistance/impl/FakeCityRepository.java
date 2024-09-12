package nl.fontys.s3.rentride_be.persistance.impl;

import nl.fontys.s3.rentride_be.persistance.CityRepository;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class FakeCityRepository implements CityRepository {
    private static long NEXT_ID = 1;

    private final List<CityEntity> savedCities;

    private FakeCityRepository() {
        this.savedCities = new ArrayList<>();
    }


    @Override
    public boolean existsByName(String name) {
        return this.savedCities
                .stream()
                .anyMatch(cityEntity -> cityEntity.getName().equals(name));
    }

    @Override
    public boolean existsById(long cityId) {
        return this.savedCities
                .stream()
                .anyMatch(cityEntity -> cityEntity.getId().equals(cityId));
    }

    @Override
    public CityEntity findById(long cityId) {
        return this.savedCities
                .stream()
                .filter(cityEntity -> cityEntity.getId() == cityId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void deleteById(long cityId) {
        this.savedCities.removeIf(cityEntity -> cityEntity.getId() == cityId);
    }

    @Override
    public CityEntity save(CityEntity city) {
        city.setId(NEXT_ID);
        NEXT_ID++;
        this.savedCities.add(city);
        return city;
    }

    @Override
    public List<CityEntity> findAll() {
        return Collections.unmodifiableList(this.savedCities);
    }

    @Override
    public int count() {
        return this.savedCities.size();
    }
}
