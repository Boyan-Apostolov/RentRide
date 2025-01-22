package nl.fontys.s3.rentride_be.business.impl.city;

import nl.fontys.s3.rentride_be.domain.city.City;
import nl.fontys.s3.rentride_be.persistance.CityRepository;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCitiesUseCaseImplTest {
    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private GetCitiesUseCaseImpl getCitiesUseCase;

    @Test
    void getCities_shouldReturnAllCities() {
        List<CityEntity> cities = new ArrayList<>();
        cities.add(
                CityEntity.builder()
                        .id(1L).name("Eindhoven").lat(50.0).lon(55.0)
                        .build()
        );
        cities.add(
                CityEntity.builder()
                        .id(2L).name("Tilburg").lat(52.0).lon(25.0)
                        .build()
        );

        when(this.cityRepository.findAll()).thenReturn(cities);

        List<City> actualCities = this.getCitiesUseCase.getCities();
        List<City> expectedCities = cities.stream().map(CityConverter::convert).toList();

        assertEquals(expectedCities, actualCities);
        verify(this.cityRepository).findAll();
    }
}