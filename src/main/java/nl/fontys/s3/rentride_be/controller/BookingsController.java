package nl.fontys.s3.rentride_be.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.booking.*;
import nl.fontys.s3.rentride_be.business.use_cases.damage.GetAllDamageUseCase;
import nl.fontys.s3.rentride_be.domain.booking.Booking;
import nl.fontys.s3.rentride_be.domain.booking.CreateBookingRequest;
import nl.fontys.s3.rentride_be.domain.booking.CreateBookingResponse;
import nl.fontys.s3.rentride_be.domain.booking.GetBookingCostsResponse;
import nl.fontys.s3.rentride_be.domain.damage.Damage;
import nl.fontys.s3.rentride_be.persistance.entity.BookingStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("bookings")
@AllArgsConstructor
public class BookingsController {
    private GetBookingsUseCase getBookingsUseCase;
    private GetBookingCosts getBookingCostsUseCase;
    private CreateBookingUseCase createBookingUseCase;
    private GetBookingsForUserUseCase getBookingsForUserUseCase;
    private UpdateBookingStatusUseCase updateBookingStatusUseCase;
    private GetAllDamageUseCase getAllDamageUseCase;

    @GetMapping
    public ResponseEntity<List<Booking>> getBookings() {
        List<Booking> allBookings = getBookingsUseCase.getBookings();

        return ResponseEntity.ok(allBookings);
    }

    @GetMapping("damages")
    ResponseEntity<List<Damage>> getPossibleDamages() {
        List<Damage> damages = getAllDamageUseCase.getAllDamage();

        return ResponseEntity.ok(damages);
    }

    @GetMapping("by-user")
    public ResponseEntity<List<Booking>> getUserBookings(@RequestParam(value = "userId") final long userId) {
        List<Booking> allBookings = getBookingsForUserUseCase.getBookingsForUser(userId);

        return ResponseEntity.ok(allBookings);
    }

    @GetMapping("cancel")
    public ResponseEntity<Void> cancelBooking(@RequestParam(value = "bookingId") final long bookingId) {
        updateBookingStatusUseCase.updateBookingStatus(bookingId, BookingStatus.Canceled);
        return ResponseEntity.ok().build();
    }

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
