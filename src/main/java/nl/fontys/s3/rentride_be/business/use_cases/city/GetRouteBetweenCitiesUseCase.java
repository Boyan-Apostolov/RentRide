package nl.fontys.s3.rentride_be.business.use_cases.city;

import nl.fontys.s3.rentride_be.domain.city.GetRouteResponse;

public interface GetRouteBetweenCitiesUseCase {
    GetRouteResponse getRoute(Long fromCityId, Long toCityId);
}
