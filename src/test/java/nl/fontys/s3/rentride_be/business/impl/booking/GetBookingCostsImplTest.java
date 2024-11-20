package nl.fontys.s3.rentride_be.business.impl.booking;

import nl.fontys.s3.rentride_be.business.use_cases.car.GetCarUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.city.GetRouteBetweenCitiesUseCase;
import nl.fontys.s3.rentride_be.configuration.security.token.AccessToken;
import nl.fontys.s3.rentride_be.domain.booking.GetBookingCostsResponse;
import nl.fontys.s3.rentride_be.domain.car.Car;
import nl.fontys.s3.rentride_be.domain.city.GetRouteResponse;
import nl.fontys.s3.rentride_be.persistance.DiscountPlanPurchaseRepository;
import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanEntity;
import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanPurchaseEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetBookingCostsImplTest {

    @Mock
    private GetRouteBetweenCitiesUseCase getRouteBetweenCitiesUseCase;

    @Mock
    private GetCarUseCase getCarUseCase;

    @Mock
    private DiscountPlanPurchaseRepository discountPlanPurchaseRepository;

    @Mock
    private AccessToken accessToken;

    @InjectMocks
    private GetBookingCostsImpl getBookingCosts;

    private GetRouteResponse routeResponse;
    private Car car;
    private DiscountPlanPurchaseEntity discountPlanPurchase;

    @BeforeEach
    void setUp() {
        routeResponse = new GetRouteResponse();
        routeResponse.setDistance("150.0"); // 150 km

        car = new Car();
        car.setFuelConsumption(8.0); // 8 liters per 100 km

        DiscountPlanEntity discountPlan = new DiscountPlanEntity();
        discountPlan.setDiscountValue(10); // 10% discount
        discountPlanPurchase = new DiscountPlanPurchaseEntity();
        discountPlanPurchase.setDiscountPlan(discountPlan);

        when(accessToken.getUserId()).thenReturn(1L);
    }

    @Test
    void getBookingCosts_ShouldCalculateCostsWithoutDiscount() {
        when(getRouteBetweenCitiesUseCase.getRoute(1L, 2L)).thenReturn(routeResponse);
        when(getCarUseCase.getCar(1L)).thenReturn(car);
        when(discountPlanPurchaseRepository.findAllByUserIdOrderByRemainingUsesDesc(1L)).thenReturn(List.of());

        GetBookingCostsResponse response = getBookingCosts.getBookingCosts(1L, 1L, 2L, 1L);

        double distance = 150.0;
        double fuelCost = (distance / 100) * car.getFuelConsumption(); // Fuel cost calculation
        double serviceCosts = fuelCost * 1.05;
        double tollFees = (fuelCost + serviceCosts) * 0.10;
        double totalWithoutDiscount = fuelCost + serviceCosts + tollFees;

        assertEquals(fuelCost, response.getFuelCost());
        assertEquals(serviceCosts, response.getServiceFees());
        assertEquals(tollFees, response.getTollFees());
        assertEquals(0.0, response.getUserDiscount()); // No discount
        assertEquals(totalWithoutDiscount, response.getTotal());
    }

    @Test
    void getBookingCosts_ShouldApplyDiscount_WhenUserHasDiscountPlan() {
        when(getRouteBetweenCitiesUseCase.getRoute(1L, 2L)).thenReturn(routeResponse);
        when(getCarUseCase.getCar(1L)).thenReturn(car);
        when(discountPlanPurchaseRepository.findAllByUserIdOrderByRemainingUsesDesc(1L)).thenReturn(List.of(discountPlanPurchase));

        GetBookingCostsResponse response = getBookingCosts.getBookingCosts(1L, 1L, 2L, 1L);

        double distance = 150.0;
        double fuelCost = (distance / 100) * car.getFuelConsumption(); // Fuel cost calculation
        double serviceCosts = fuelCost * 1.05;
        double tollFees = (fuelCost + serviceCosts) * 0.10;
        double totalWithoutDiscount = fuelCost + serviceCosts + tollFees;
        double userDiscount = totalWithoutDiscount * (discountPlanPurchase.getDiscountPlan().getDiscountValue() / 100.0);

        assertEquals(fuelCost, response.getFuelCost());
        assertEquals(serviceCosts, response.getServiceFees());
        assertEquals(tollFees, response.getTollFees());
        assertEquals(userDiscount, response.getUserDiscount()); // Discount applied
        assertEquals(totalWithoutDiscount - userDiscount, response.getTotal());
    }

    @Test
    void getBookingCosts_ShouldHandleEmptyRouteResponseGracefully() {
        when(getRouteBetweenCitiesUseCase.getRoute(1L, 2L)).thenReturn(new GetRouteResponse());
        when(getCarUseCase.getCar(1L)).thenReturn(car);
        when(discountPlanPurchaseRepository.findAllByUserIdOrderByRemainingUsesDesc(1L)).thenReturn(List.of());

        GetBookingCostsResponse response = getBookingCosts.getBookingCosts(1L, 1L, 2L, 1L);

        assertEquals(0.0, response.getFuelCost());
        assertEquals(0.0, response.getServiceFees());
        assertEquals(0.0, response.getTollFees());
        assertEquals(0.0, response.getUserDiscount());
        assertEquals(0.0, response.getTotal());
    }
}