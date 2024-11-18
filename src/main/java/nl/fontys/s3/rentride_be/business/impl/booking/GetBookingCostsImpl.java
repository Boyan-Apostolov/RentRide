package nl.fontys.s3.rentride_be.business.impl.booking;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.booking.GetBookingCosts;
import nl.fontys.s3.rentride_be.business.use_cases.car.GetCarUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.city.GetRouteBetweenCitiesUseCase;
import nl.fontys.s3.rentride_be.configuration.security.token.AccessToken;
import nl.fontys.s3.rentride_be.domain.booking.GetBookingCostsResponse;
import nl.fontys.s3.rentride_be.domain.car.Car;
import nl.fontys.s3.rentride_be.domain.city.GetRouteResponse;
import nl.fontys.s3.rentride_be.persistance.DiscountPlanPurchaseRepository;
import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanPurchaseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class GetBookingCostsImpl implements GetBookingCosts {
    private GetRouteBetweenCitiesUseCase getRouteBetweenCitiesUseCase;
    private GetCarUseCase getCarUseCase;
    private DiscountPlanPurchaseRepository discountPlanPurchaseRepository;

    private AccessToken requestAccessToken;

    @Override
    public GetBookingCostsResponse getBookingCosts(long carId, long fromCityId, long toCityId, long userId) {
        Long currentUserId = requestAccessToken.getUserId();
        GetRouteResponse routeData = getRouteBetweenCitiesUseCase.getRoute(fromCityId, toCityId);
        double distance = routeData.getDistance() != null ? Double.parseDouble(routeData.getDistance()) : 0.0;

        Car car = getCarUseCase.getCar(carId);
        double fuelCost = (distance / 100) * car.getFuelConsumption();
        double serviceCosts = fuelCost * 1.05;
        double tollFees = (fuelCost + serviceCosts) * 0.10;
        double totalWithoutDiscount = fuelCost + serviceCosts + tollFees;

        List<DiscountPlanPurchaseEntity> purchasedPlans = discountPlanPurchaseRepository.findAllByUserIdOrderByRemainingUsesDesc(currentUserId);
        Optional<DiscountPlanPurchaseEntity> currentPlanOptional = purchasedPlans.stream().findFirst();

        double userDiscounts = currentPlanOptional.map(discountPlanPurchaseEntity -> (totalWithoutDiscount * discountPlanPurchaseEntity.getDiscountPlan().getDiscountValue() / 100)).orElse(0.0);

        return GetBookingCostsResponse.builder()
                .fuelCost(fuelCost)
                .serviceFees(serviceCosts)
                .tollFees(tollFees)
                .userDiscount(userDiscounts)
                .total(totalWithoutDiscount - userDiscounts)
                .build();
    }
}
