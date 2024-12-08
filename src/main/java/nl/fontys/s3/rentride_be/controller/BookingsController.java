package nl.fontys.s3.rentride_be.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.auth.EmailerUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.booking.*;
import nl.fontys.s3.rentride_be.business.use_cases.damage.GetAllDamageUseCase;
import nl.fontys.s3.rentride_be.domain.booking.*;
import nl.fontys.s3.rentride_be.domain.damage.Damage;
import nl.fontys.s3.rentride_be.domain.payment.Payment;
import nl.fontys.s3.rentride_be.domain.user.EmailType;
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
    private EmailerUseCase emailerUseCase;

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

    @GetMapping("/count")
    public ResponseEntity<Long> getAllCountByUser() {
        Long count = this.getBookingsForUserUseCase.getCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/paged")
    public ResponseEntity<List<Booking>> getPagedBookings(@RequestParam(defaultValue = "0") int page) {
        List<Booking> userBookings = getBookingsForUserUseCase.getBookingsForUser(page);

        return ResponseEntity.ok(userBookings);
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

    @PostMapping("calculate-cost")
    public GetBookingCostsResponse getBookingCosts(@RequestBody @Valid GetBookingCostsRequest request) {
        return getBookingCostsUseCase.getBookingCosts(request);
    }

    @PostMapping()
    public ResponseEntity<CreateBookingResponse> createBooking(@RequestBody @Valid CreateBookingRequest request) {
        CreateBookingResponse response = createBookingUseCase.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/claim")
    public ResponseEntity<CreateBookingResponse> claimAuctionBooking(@RequestBody @Valid CreateBookingRequest request) {
        CreateBookingResponse response = createBookingUseCase.createBooking(request);

        emailerUseCase.send(response.getBooking().getUser().getEmail(), "Booking reserved!",
                String.format("Your booking with number #%s from %s to %s has been successfully registered! You will receive a second confirmation email when the booking starts!",
                        response.getBooking().getId(), response.getBooking().getStartCity().getName(), response.getBooking().getEndCity().getName()),
                EmailType.BOOKING);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("by-car-map")
    public ResponseEntity<String> getCarBookingsMap(@RequestParam(value = "carId") final long carId) {
        List<Booking> carBookings = getCarBookings(carId).getBody();

        String mapUrl = getBookingHistoryMapUseCase.getBookingHistoryMap(carBookings);

        return ResponseEntity.ok(mapUrl);
    }
}
