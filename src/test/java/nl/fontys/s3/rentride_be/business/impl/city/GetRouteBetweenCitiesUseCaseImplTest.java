package nl.fontys.s3.rentride_be.business.impl.city;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.persistance.CityRepository;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetRouteBetweenCitiesUseCaseImplTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private GetRouteBetweenCitiesUseCaseImpl getRouteBetweenCitiesUseCase;

    private CityEntity fromCity;
    private CityEntity toCity;

    @BeforeEach
    void setUp() {
        fromCity = new CityEntity();
        fromCity.setId(1L);
        fromCity.setLat(52.3702);
        fromCity.setLon(4.8952);

        toCity = new CityEntity();
        toCity.setId(2L);
        toCity.setLat(51.5074);
        toCity.setLon(-0.1278);
    }

    @Test
    void getRoute_ShouldThrowNotFoundException_WhenCityNotFound() {
        when(cityRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> getRouteBetweenCitiesUseCase.getRoute(1L, 2L));

        verify(cityRepository, times(1)).findById(1L);
        verify(cityRepository, never()).findById(2L);
    }

    @Test
    void getRoute_ShouldThrowNotFoundException_WhenApiCallFails() {
        when(cityRepository.findById(1L)).thenReturn(Optional.of(fromCity));
        when(cityRepository.findById(2L)).thenReturn(Optional.of(toCity));

        when(restTemplate.getForEntity(anyString(), eq(String.class)))
                .thenThrow(new RuntimeException("API call failed"));

        assertThrows(NotFoundException.class, () -> getRouteBetweenCitiesUseCase.getRoute(1L, 2L));

        verify(cityRepository, times(1)).findById(1L);
        verify(cityRepository, times(1)).findById(2L);
    }
}