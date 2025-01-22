package nl.fontys.s3.rentride_be.business.impl.city;

import lombok.RequiredArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.city.LookupCityUseCase;
import nl.fontys.s3.rentride_be.domain.city.GeoapifyResponse;
import nl.fontys.s3.rentride_be.domain.city.GeoapifyResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.InvalidObjectException;

@Service
@RequiredArgsConstructor
public class LookupCityUseCaseImpl implements LookupCityUseCase {
    private static final Logger logger = LoggerFactory.getLogger(LookupCityUseCaseImpl.class);

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
                throw new InvalidObjectException("Invalid response from api");
            }

            return geoapifyResponse
                    .getResults()
                    .get(0);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }

    }
}
