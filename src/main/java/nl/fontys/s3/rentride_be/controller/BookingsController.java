package nl.fontys.s3.rentride_be.controller;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.useCases.booking.GetBookingCosts;
import nl.fontys.s3.rentride_be.domain.booking.GetBookingCostsResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("bookings")
@AllArgsConstructor
public class BookingsController {
    private GetBookingCosts getBookingCostsUseCase;

    @GetMapping("calculate-cost")
    public GetBookingCostsResponse getBookingCosts(@RequestParam(value = "carId") final long carId,
                                                   @RequestParam(value = "fromCityId") final long fromCityId,
                                                   @RequestParam(value = "toCityId") final long toCityId) {
        return getBookingCostsUseCase.getBookingCosts(carId, fromCityId, toCityId, 0);
    }
}
