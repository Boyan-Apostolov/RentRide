package nl.fontys.s3.rentride_be.controller;

import jakarta.annotation.security.RolesAllowed;
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
    private GetBookingsForCarUseCase getBookingsForCarUseCase;
    private UpdateBookingStatusUseCase updateBookingStatusUseCase;
    private GetAllDamageUseCase getAllDamageUseCase;
    private GetBookingHistoryMapUseCase getBookingHistoryMapUseCase;

    @GetMapping
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<List<Booking>> getBookings() {
        List<Booking> allBookings = getBookingsUseCase.getBookings();

        return ResponseEntity.ok(allBookings);
    }

    @GetMapping("damages")
    @RolesAllowed({"ADMIN"})
    ResponseEntity<List<Damage>> getPossibleDamages() {
        List<Damage> damages = getAllDamageUseCase.getAllDamage();

        return ResponseEntity.ok(damages);
    }

    @GetMapping("by-user")
    public ResponseEntity<List<Booking>> getUserBookings() {
        List<Booking> allBookings = getBookingsForUserUseCase.getBookingsForUser();

        return ResponseEntity.ok(allBookings);
    }

    @GetMapping("by-car")
    public ResponseEntity<List<Booking>> getCarBookings(@RequestParam(value = "carId") final long carId) {
        List<Booking> allBookings = getBookingsForCarUseCase.getBookings(carId);

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
        return getBookingCostsUseCase.getBookingCosts(carId, fromCityId, toCityId);
    }

    @PostMapping()
    public ResponseEntity<CreateBookingResponse> createBooking(@RequestBody @Valid CreateBookingRequest request) {
        CreateBookingResponse response = createBookingUseCase.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("by-car-map")
    public ResponseEntity<String> getCarBookingsMap(@RequestParam(value = "carId") final long carId) {
        List<Booking> carBookings = getCarBookings(carId).getBody();

        String mapUrl = getBookingHistoryMapUseCase.getBookingHistoryMap(carBookings);

        return ResponseEntity.ok(mapUrl);
    }
}
