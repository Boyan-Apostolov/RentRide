package nl.fontys.s3.rentride_be.business.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import nl.fontys.s3.rentride_be.business.useCases.city.LookupCityUseCase;
import nl.fontys.s3.rentride_be.domain.city.GeoapifyResponse;
import nl.fontys.s3.rentride_be.domain.city.GeoapifyResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class LookupCityUseCaseImpl implements LookupCityUseCase {
    @Value("${API_KEY_GEOAPIFY}")
    private String geoapifyApiKey;

    private final RestTemplate restTemplate;

    @Override
    public GeoapifyResult lookupCity(String cityName) {
        String url = String.format(
                "https://api.geoapify.com/v1/geocode/search?text=%s&format=json&apiKey=%s",
                cityName,
                geoapifyApiKey
        );

        try {
            ResponseEntity<GeoapifyResponse> response = restTemplate
                    .getForEntity(url, GeoapifyResponse.class);
            GeoapifyResponse geoapifyResponse = response.getBody();

            if (geoapifyResponse == null || geoapifyResponse.getResults().isEmpty()) {
                throw new Exception("Invalid response from api");
            }

            return geoapifyResponse
                    .getResults()
                    .get(0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

    }
}
