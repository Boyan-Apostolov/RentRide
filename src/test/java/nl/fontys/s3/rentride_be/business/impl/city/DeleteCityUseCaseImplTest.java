package nl.fontys.s3.rentride_be.business.impl.city;

import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.persistance.CityRepository;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteCityUseCaseImplTest {

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private DeleteCityUseCaseImpl deleteCityUseCase;

    @Test
    void deleteCity_shouldCorrectlyDeleteTheCity() {
        when(this.cityRepository.existsById(1L)).thenReturn(true);

        this.deleteCityUseCase.deleteCity(1L);

        verify(this.cityRepository).deleteById(1L);
    }

    @Test
    void deleteCity_shouldThrowErrorIfCityDoesNotExist() {
        assertThrows(NotFoundException.class, () -> this.deleteCityUseCase.deleteCity(1L));

        verify(this.cityRepository).existsById(1L);
    }
}