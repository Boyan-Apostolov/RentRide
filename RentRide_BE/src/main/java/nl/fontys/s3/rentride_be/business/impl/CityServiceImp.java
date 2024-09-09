package nl.fontys.s3.rentride_be.business.impl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.CityService;
import nl.fontys.s3.rentride_be.domain.City;
import nl.fontys.s3.rentride_be.persistance.CityRepo;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class CityServiceImp implements CityService {
    private CityRepo cityRepo;

    @Override
    public List<City> getCities() {
        return Collections.unmodifiableList(this.cityRepo.getCities());
    }

    @Override
    public void addCity(City city) {
        this.cityRepo.addCity(city);
    }
}
