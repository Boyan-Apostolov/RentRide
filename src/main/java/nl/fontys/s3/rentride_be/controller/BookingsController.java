package nl.fontys.s3.rentride_be.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.booking.CreateBookingUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.booking.GetBookingCosts;
import nl.fontys.s3.rentride_be.domain.booking.CreateBookingRequest;
import nl.fontys.s3.rentride_be.domain.booking.CreateBookingResponse;
import nl.fontys.s3.rentride_be.domain.booking.GetBookingCostsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("bookings")
@AllArgsConstructor
public class BookingsController {
    private GetBookingCosts getBookingCostsUseCase;
    private CreateBookingUseCase createBookingUseCase;

    @GetMapping("calculate-cost")
    public GetBookingCostsResponse getBookingCosts(@RequestParam(value = "carId") final long carId,
                                                   @RequestParam(value = "fromCityId") final long fromCityId,
                                                   @RequestParam(value = "toCityId") final long toCityId) {
        return getBookingCostsUseCase.getBookingCosts(carId, fromCityId, toCityId, 0);
    }

    @PostMapping()
    public ResponseEntity<CreateBookingResponse> createBooking(@RequestBody @Valid CreateBookingRequest request) {
        CreateBookingResponse response = createBookingUseCase.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
