package nl.fontys.s3.rentride_be.business.useCases.city;

import nl.fontys.s3.rentride_be.domain.city.GetRouteResponse;

public interface GetRouteBetweenCitiesUseCase {
    GetRouteResponse getRoute(String fromCity, String toCity);
}
