package nl.fontys.s3.rentride_be.business.impl.city;

import nl.fontys.s3.rentride_be.domain.city.City;
import nl.fontys.s3.rentride_be.persistance.CityRepository;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCityUseCaseImplTest {

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private GetCityUseCaseImpl getCityUseCase;

    @Test
    void getCity_ShouldReturnNullWhenCityDoesNotExist() {
        City foundCity = this.getCityUseCase.getCity(1L);

        assertNull(foundCity);

        verify(this.cityRepository).findById(1L);
    }

    @Test
    void getCity_ShouldReturnCityWithCorrectIdExists() {
        Optional<CityEntity> city = Optional.of(CityEntity.builder()
                .id(1L).name("Eindhoven").lat(50.0).lon(55.0)
                .build());

        when(this.cityRepository.findById(1L)).thenReturn(city);

        City foundCity = this.getCityUseCase.getCity(1L);

        assertEquals(CityConverter.convert(city.get()), foundCity);

        verify(this.cityRepository).findById(1L);
    }
}