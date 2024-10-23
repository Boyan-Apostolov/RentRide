package nl.fontys.s3.rentride_be.business.impl.city;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetRouteBetweenCitiesUseCaseImpl implements GetRouteBetweenCitiesUseCase {

    @Value("${API_KEY_GEOAPIFY}")
    private String geoapifyApiKey;

    private final RestTemplate restTemplate;
    private final CityRepository cityRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public GetRouteResponse getRoute(Long fromCityId, Long toCityId) {
        CityEntity fromCityEntity = findCityById(fromCityId);
        CityEntity toCityEntity = findCityById(toCityId);

        String routeMapUrl = createRouteMapUrl(fromCityEntity, toCityEntity);
        String routingDistanceUrl = createRoutingDistanceUrl(fromCityEntity, toCityEntity);

        String[] routeArr = getRouteDistanceFromApi(routingDistanceUrl);
        String distance = routeArr[0];
        String time = routeArr[1];

        return GetRouteResponse.builder()
                .distance(distance)
                .time(time)
                .imgUrl(routeMapUrl)
                .build();
    }

    private CityEntity findCityById(Long cityId) {
        return cityRepository.findById(cityId)
                .orElseThrow(() -> new NotFoundException("City not found: " + cityId));
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

    private String[] getRouteDistanceFromApi(String routingDistanceUrl) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(routingDistanceUrl, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();

                JsonNode root = objectMapper.readTree(responseBody);
                JsonNode featuresNode = root.path("features").get(0);
                JsonNode legsNode = featuresNode.path("properties").path("legs").get(0);

                double distance = legsNode.path("distance").asDouble();
                double time = legsNode.path("time").asDouble();

                return new String[]{
                        String.format("%.2f", distance / 1000),
                        String.format("%.2f", time / 60 / 60)};
            } else {
                throw new NotFoundException("Error fetching route distance: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new NotFoundException("Failed to parse route distance from API response");
        }
    }
}