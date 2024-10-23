package nl.fontys.s3.rentride_be.business.impl.booking;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.useCases.booking.GetBookingCosts;
import nl.fontys.s3.rentride_be.business.useCases.car.GetCarUseCase;
import nl.fontys.s3.rentride_be.business.useCases.city.GetRouteBetweenCitiesUseCase;
import nl.fontys.s3.rentride_be.domain.booking.GetBookingCostsResponse;
import nl.fontys.s3.rentride_be.domain.car.Car;
import nl.fontys.s3.rentride_be.domain.city.GetRouteResponse;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class GetBookingCostsImpl implements GetBookingCosts {
    private GetRouteBetweenCitiesUseCase getRouteBetweenCitiesUseCase;
    private GetCarUseCase getCarUseCase;

    @Override
    public GetBookingCostsResponse getBookingCosts(long carId, long fromCityId, long toCityId, long userId) {
        GetRouteResponse routeData = getRouteBetweenCitiesUseCase.getRoute(fromCityId, toCityId);
        double distance = Double.parseDouble(routeData.getDistance());

        Car car = getCarUseCase.getCar(carId);
        double fuelCost = (distance / 100) * car.getFuelConsumption();
        double serviceCosts = fuelCost * 1.05;
        double tollFees = (fuelCost + serviceCosts) * 0.10;
        double userDiscounts = 0; //TODO: Implement
        return GetBookingCostsResponse.builder()
                .fuelCost(fuelCost)
                .serviceFees(serviceCosts)
                .tollFees(tollFees)
                .userDiscount(userDiscounts)
                .total(fuelCost + serviceCosts + tollFees + userDiscounts)
                .build();
    }
}
