package nl.fontys.s3.rentride_be.business.impl.city;

import nl.fontys.s3.rentride_be.domain.city.GeoapifyResponse;
import nl.fontys.s3.rentride_be.domain.city.GeoapifyResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LookupCityUseCaseImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private LookupCityUseCaseImpl lookupCityUseCase;

    @Value("${API_KEY_GEOAPIFY}")
    private String geoapifyApiKey;

    @Test
    void lookupCity_ShouldReturnGeoapifyResult_WhenCityIsFound() {
        String cityName = "Amsterdam";
        GeoapifyResult expectedResult = new GeoapifyResult();
        expectedResult.setCity("Amsterdam");
        expectedResult.setLat(52.3676);
        expectedResult.setLon(4.9041);

        GeoapifyResponse geoapifyResponse = new GeoapifyResponse();
        geoapifyResponse.setResults(List.of(expectedResult));

        ResponseEntity<GeoapifyResponse> responseEntity = new ResponseEntity<>(geoapifyResponse, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), eq(GeoapifyResponse.class))).thenReturn(responseEntity);

        GeoapifyResult actualResult = lookupCityUseCase.lookupCity(cityName);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void lookupCity_ShouldReturnNull_WhenNoResultsAreFound() {
        String cityName = "UnknownCity";

        GeoapifyResponse geoapifyResponse = new GeoapifyResponse();
        geoapifyResponse.setResults(Collections.emptyList());

        ResponseEntity<GeoapifyResponse> responseEntity = new ResponseEntity<>(geoapifyResponse, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), eq(GeoapifyResponse.class))).thenReturn(responseEntity);

        GeoapifyResult result = lookupCityUseCase.lookupCity(cityName);

        assertNull(result);
    }

    @Test
    void lookupCity_ShouldReturnNull_WhenExceptionIsThrown() {
        String cityName = "Amsterdam";

        when(restTemplate.getForEntity(anyString(), eq(GeoapifyResponse.class)))
                .thenThrow(new RuntimeException("API error"));

        GeoapifyResult result = lookupCityUseCase.lookupCity(cityName);

        assertNull(result);
    }
}