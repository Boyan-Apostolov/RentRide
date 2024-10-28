package nl.fontys.s3.rentride_be.repository;

import jakarta.persistence.EntityManager;
import nl.fontys.s3.rentride_be.persistance.CityRepository;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class CityRepositoryTest {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private CityRepository cityRepository;

    @Test
    void save_ShouldSaveCityWithAllFields(){
        CityEntity initialCity = CityEntity.builder()
                .name("Eindhoven")
                .lat(10.0)
                .lon(10.0)
                .depoAdress("Test str")
                .build();

        CityEntity savedCity = cityRepository.save(initialCity);
        assertNotNull(savedCity.getId());

        savedCity = entityManager.find(CityEntity.class, savedCity.getId());
        initialCity.setId(savedCity.getId());

        assertEquals(initialCity, savedCity);
    }

    @Test
    void findById_ShouldFindCity(){
        CityEntity initialCity = CityEntity.builder()
                .name("Eindhoven")
                .lat(10.0)
                .lon(10.0)
                .depoAdress("Test str")
                .build();
        CityEntity savedCity = cityRepository.save(initialCity);

        Optional<CityEntity> foundCity = cityRepository.findById(savedCity.getId());

        assertTrue(foundCity.isPresent());
    }

    @Test
    void existsByName_ShouldReturnTrueForExistingCity(){
        cityRepository.save(CityEntity.builder()
                .name("Eindhoven")
                .lat(10.0)
                .lon(10.0)
                .depoAdress("Test str")
                .build());

        boolean cityExists = cityRepository.existsByName("Eindhoven");

        assertTrue(cityExists);
    }

    @Test
    void existsByName_ShouldReturnFalseForFakeCity(){
        boolean cityExists = cityRepository.existsByName("fake city");

        assertFalse(cityExists);
    }

    @Test
    void findByName_ShouldFindCity(){
        cityRepository.save(CityEntity.builder()
                .name("Eindhoven")
                .lat(10.0)
                .lon(10.0)
                .depoAdress("Test str")
                .build());

        CityEntity foundCity = cityRepository.findByName("Eindhoven");

        assertNotNull(foundCity);
    }

    @Test
    void findByName_ShouldReturnNullForFakeCity(){
        CityEntity foundCity = cityRepository.findByName("fake city");

        assertNull(foundCity);
    }
}
