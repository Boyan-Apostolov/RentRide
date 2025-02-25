package nl.fontys.s3.rentride_be.business.impl.city;

import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.domain.city.UpdateCityRequest;
import nl.fontys.s3.rentride_be.persistance.CityRepository;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCityUseCaseImplTest {

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private UpdateCityUseCaseImpl updateCityUseCase;

    @Test
    void updateCity_withInvalidIdShouldThrowException() {
        when(this.cityRepository.findById(1L))
                .thenThrow(new NotFoundException("CITY_NOT_FOUND"));

        UpdateCityRequest request = UpdateCityRequest.builder()
                .id(1L)
                .name("test")
                .lat(12.12)
                .lon(12.12)
                .build();

        assertThrows(NotFoundException.class, () -> this.updateCityUseCase.updateCity(request));

        verify(this.cityRepository).findById(1L);
    }

    @Test
    void updateCity_withValidIdShouldUpdateTheEntity() {
        Long cityId = 1L;

        Optional<CityEntity> existingCity = Optional.of(CityEntity.builder()
                .id(cityId).name("Eindhoven").lat(50.0).lon(55.0)
                .build());

        CityEntity updatedCity = CityEntity.builder()
                .id(cityId).name("Eindhoven-update").lat(55.0).lon(60.0)
                .build();

        when(this.cityRepository.findById(cityId)).thenReturn(existingCity);
        when(this.cityRepository.save(any(CityEntity.class))).thenReturn(updatedCity);

        this.updateCityUseCase.updateCity(
                UpdateCityRequest.builder()
                        .id(cityId).name("Eindhoven-update").lat(55.0).lon(60.0)
                        .build()
        );


        assertEquals("Eindhoven-update", existingCity.get().getName());
        assertEquals(55.0, existingCity.get().getLat());
        assertEquals(60.0, existingCity.get().getLon());

        verify(this.cityRepository).save(existingCity.get());
        verify(this.cityRepository, times(2)).findById(cityId);
    }
}