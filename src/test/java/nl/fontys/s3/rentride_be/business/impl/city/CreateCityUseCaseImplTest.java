package nl.fontys.s3.rentride_be.business.impl.city;

import nl.fontys.s3.rentride_be.business.exception.AlreadyExistsException;
import nl.fontys.s3.rentride_be.domain.city.CreateCityRequest;
import nl.fontys.s3.rentride_be.domain.city.CreateCityResponse;
import nl.fontys.s3.rentride_be.persistance.CityRepository;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCityUseCaseImplTest {

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CreateCityUseCaseImpl createCityUseCase;

    @Test
    void createCity_withDuplicatedName_shouldThrowException() {
        when(this.cityRepository.existsByName("Eindhoven"))
                .thenThrow(new AlreadyExistsException("City"));

        assertThrows(AlreadyExistsException.class, () ->
                this.createCityUseCase.createCity(
                        CreateCityRequest.builder()
                                .name("Eindhoven")
                                .lon(53.3)
                                .lat(55.9)
                                .build()
                )
        );

        verify(this.cityRepository).existsByName("Eindhoven");
    }

    @Test
    void createCity_shouldCorrectlyAddTheCityWithValidName() {
        CityEntity cityEntity = CityEntity.builder()
                .id(1L)
                .name("Eindhoven")
                .lon(53.3)
                .lat(55.9)
                .build();

        when(this.cityRepository.save(any(CityEntity.class))).thenReturn(cityEntity);

        CreateCityResponse createdCity = this.createCityUseCase.createCity(
                CreateCityRequest.builder()
                        .name("Eindhoven")
                        .lon(53.3)
                        .lat(55.9)
                        .build()
        );

        assertEquals(cityEntity.getId(), createdCity.getCityId());

       verify(this.cityRepository).save(any(CityEntity.class));
    }
}