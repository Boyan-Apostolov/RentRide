package nl.fontys.s3.rentride_be.controller;

import com.stripe.model.checkout.Session;
import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.useCases.booking.GetBookingByIdUseCase;
import nl.fontys.s3.rentride_be.business.useCases.booking.UpdateBookingStatusUseCase;
import nl.fontys.s3.rentride_be.business.useCases.payment.CreatePaymentSessionUseCase;
import nl.fontys.s3.rentride_be.domain.booking.Booking;
import nl.fontys.s3.rentride_be.domain.booking.UpdateBookingStatusRequest;
import nl.fontys.s3.rentride_be.persistance.entity.BookingStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("payments")
@AllArgsConstructor
public class PaymentsController {

   private CreatePaymentSessionUseCase createPaymentSessionUseCase;
   private GetBookingByIdUseCase getBookingByIdUseCase;
   private UpdateBookingStatusUseCase updateBookingStatusUseCase;

    @GetMapping("/success")
    public ResponseEntity success(@RequestParam("sessionId") String sessionId,
                                  @RequestParam("bookingId") Long bookingId) {
        try {
            Session session = Session.retrieve(sessionId);
            String paymentStatus = session.getPaymentStatus();

            if (Objects.equals(paymentStatus, "paid")) {
                updateBookingStatusUseCase.updateBookingStatus(bookingId, BookingStatus.Paid);
                return ResponseEntity.accepted().build();
            } else {
                updateBookingStatusUseCase.updateBookingStatus(bookingId, BookingStatus.Canceled);
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/cancel")
    public ResponseEntity cancel(@RequestParam("bookingId") Long bookingId) {
        updateBookingStatusUseCase.updateBookingStatus(bookingId, BookingStatus.Canceled);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pay")
    public ResponseEntity<String> createCheckoutSession(@RequestParam("bookingId") Long bookingId) {
        Booking booking = getBookingByIdUseCase.getBookingById(bookingId);

        String paymentDescription = String.format("Booking - (%s) -> (%s)", booking.getStartCity().getName(), booking.getEndCity().getName());
        try {
            String url = this.createPaymentSessionUseCase.createPaymentSession(paymentDescription, booking);

            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}