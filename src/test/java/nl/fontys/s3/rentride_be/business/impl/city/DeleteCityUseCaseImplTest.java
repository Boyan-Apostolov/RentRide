package nl.fontys.s3.rentride_be.business.impl.city;

import nl.fontys.s3.rentride_be.persistance.CityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteCityUseCaseImplTest {

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private DeleteCityUseCaseImpl deleteCityUseCase;

    @Test
    void deleteCity_shouldCorrectlyDeleteTheCity() {
        this.deleteCityUseCase.deleteCity(1L);

        verify(this.cityRepository).deleteById(1L);
    }

    @Test
    void deleteCity_shouldThrowErrorIfCityHasCars() {
        //TODO: Implement
    }
}