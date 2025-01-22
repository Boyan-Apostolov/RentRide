package nl.fontys.s3.rentride_be.persistance;

import jakarta.persistence.EntityManager;
import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class CarRepositoryTest {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private CityRepository cityRepository;

    CityEntity saveCity(){
        CityEntity cityEntity = CityEntity.builder()
                .depoAdress("")
                .lon(2.23)
                .lat(2.23)
                .name("")
                .build();
        return cityRepository.save(cityEntity);
    }

    @Test
    void save_ShouldSaveCarWithAllFields() {
        CarEntity initialCar = CarEntity.builder()
                .model("Model S")
                .city(saveCity())                .make("test")
                .fuelConsumption(1.2)
                .registrationNumber("ABC123")
                .features(List.of())
                .isExclusive(false)
                .build();

        CarEntity savedCar = carRepository.save(initialCar);
        assertNotNull(savedCar.getId());

        savedCar = entityManager.find(CarEntity.class, savedCar.getId());
        initialCar.setId(savedCar.getId());

        assertEquals(initialCar, savedCar);
    }

    @Test
    void findById_ShouldFindCar() {
        saveCity();
        CarEntity initialCar = CarEntity.builder()
                .model("Model S")
                .city(saveCity())                .make("test")
                .fuelConsumption(1.2)
                .registrationNumber("ABC123")
                .features(List.of())
                .isExclusive(false)
                .build();
        CarEntity savedCar = carRepository.save(initialCar);

        Optional<CarEntity> foundCar = carRepository.findById(savedCar.getId());

        assertTrue(foundCar.isPresent());
        assertEquals(savedCar, foundCar.get());
    }

    @Test
    void existsByRegistrationNumber_ShouldReturnTrueForExistingCar() {
        saveCity();

        carRepository.save(CarEntity.builder()
                .model("Model S")
                .city(saveCity())
                .make("test")
                .fuelConsumption(1.2)
                .registrationNumber("ABC123")
                .features(List.of())
                .isExclusive(false)
                .build());

        boolean carExists = carRepository.existsByRegistrationNumber("ABC123");

        assertTrue(carExists);
    }

    @Test
    void existsByRegistrationNumber_ShouldReturnFalseForNonExistingCar() {
        boolean carExists = carRepository.existsByRegistrationNumber("XYZ987");

        assertFalse(carExists);
    }
}