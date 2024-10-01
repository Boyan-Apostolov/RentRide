package nl.fontys.s3.rentride_be.business.impl.city;

import lombok.RequiredArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.useCases.city.GetRouteBetweenCitiesUseCase;
import nl.fontys.s3.rentride_be.domain.city.GetRouteResponse;
import nl.fontys.s3.rentride_be.persistance.CityRepository;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GetRouteBetweenCitiesUseCaseImpl implements GetRouteBetweenCitiesUseCase {

    @Value("${API_KEY_GEOAPIFY}")
    private String geoapifyApiKey;

    private final RestTemplate restTemplate;
    private final CityRepository cityRepository;

    @Override
    public GetRouteResponse getRoute(String fromCity, String toCity) {
        CityEntity fromCityEntity = findCityByName(fromCity);
        CityEntity toCityEntity = findCityByName(toCity);

        String routeMapUrl = createRouteMapUrl(fromCityEntity, toCityEntity);
        String routingDistanceUrl = createRoutingDistanceUrl(fromCityEntity, toCityEntity);

        String responseBody = getRouteDistanceFromApi(routingDistanceUrl);

        return GetRouteResponse.builder()
                .responseBody(responseBody)
                .imgUrl(routeMapUrl)
                .build();
    }

    private CityEntity findCityByName(String cityName) {
        CityEntity cityEntity = cityRepository.findByName(cityName);
        if (cityEntity == null) {
            throw new NotFoundException("City not found: " + cityName);
        }
        return cityEntity;
    }

    private String createRouteMapUrl(CityEntity fromCityEntity, CityEntity toCityEntity) {
        return String.format(
                "https://maps.geoapify.com/v1/staticmap?style=osm-bright&width=600&height=400"
                        + "&marker=lonlat:%.6f,%.6f" // Marker for 'from' city
                        + "&marker=lonlat:%.6f,%.6f" // Marker for 'to' city
                        + "&geometry=polyline:%.6f,%.6f,%.6f,%.6f" // Polyline for the route
                        + "&apiKey=%s",
                fromCityEntity.getLon(), fromCityEntity.getLat(), // 'From' city coordinates
                toCityEntity.getLon(), toCityEntity.getLat(),     // 'To' city coordinates
                fromCityEntity.getLon(), fromCityEntity.getLat(), // Polyline start
                toCityEntity.getLon(), toCityEntity.getLat(),     // Polyline end
                geoapifyApiKey);
    }

    private String createRoutingDistanceUrl(CityEntity fromCityEntity, CityEntity toCityEntity) {
        return String.format("https://api.geoapify.com/v1/routing" +
                        "?waypoints=%.6f,%.6f|%.6f,%.6f" +
                        "&mode=drive" +
                        "&apiKey=%s",
                fromCityEntity.getLat(), fromCityEntity.getLon(),
                toCityEntity.getLat(), toCityEntity.getLon(),
                geoapifyApiKey);
    }

    private String getRouteDistanceFromApi(String routingDistanceUrl) {
        ResponseEntity<String> response = restTemplate.getForEntity(routingDistanceUrl, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException("Error fetching route distance: " + response.getStatusCode());
        }
    }
}