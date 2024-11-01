package nl.fontys.s3.rentride_be.controller;

import com.stripe.model.checkout.Session;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.booking.GetBookingByIdUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.booking.ScheduleBookingJobsUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.booking.UpdateBookingStatusUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.payment.*;
import nl.fontys.s3.rentride_be.domain.booking.Booking;
import nl.fontys.s3.rentride_be.domain.payment.CreatePaymentRequest;
import nl.fontys.s3.rentride_be.domain.payment.Payment;
import nl.fontys.s3.rentride_be.persistance.entity.BookingStatus;
import nl.fontys.s3.rentride_be.persistance.entity.PaymentEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("payments")
@AllArgsConstructor
public class PaymentsController {

    private CreatePaymentSessionUseCase createPaymentSessionUseCase;
    private GetBookingByIdUseCase getBookingByIdUseCase;
    private UpdateBookingStatusUseCase updateBookingStatusUseCase;
    private CreatePaymentUseCase createPaymentUseCase;
    private UpdatePaymentUseCase updatePaymentUseCase;
    private GetPaymentsByUser getPaymentsByUser;
    private SetPaymentToPaid setPaymentToPaid;

    private ScheduleBookingJobsUseCase scheduleBookingJobsUseCase;

    @GetMapping("by-user")
    public ResponseEntity<List<Payment>> getPaymentsByUser(@RequestParam("userId") Long userId) {
        //TODO: Use session to validate if user has same id
        List<Payment> userPayments = getPaymentsByUser.getPaymentsByUser(userId);

        return ResponseEntity.ok(userPayments);
    }

    @PostMapping("create-request")
    public ResponseEntity<String> createPaymentRequest(@RequestBody @Valid CreatePaymentRequest request) {
        try {
            PaymentEntity paymentEntity = createPaymentUseCase.createPayment(request);

            String paymentUrl = createPaymentSessionUseCase.createPaymentSession(request.getDescription(), request.getTotalCost(), "payment", paymentEntity.getId());

            paymentEntity.setStripeLink(paymentUrl);
            updatePaymentUseCase.updatePayment(paymentEntity);

            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/success")
    public ResponseEntity<String> success(@RequestParam("sessionId") String sessionId,
                                          @RequestParam("paymentType") String paymentType,
                                          @RequestParam("entityId") Long entityId) {
        try {
            Session session = Session.retrieve(sessionId);
            String paymentStatus = session.getPaymentStatus();
            String paymentId = session.getPaymentIntent();

            if(paymentType.equals("booking")){
                Booking booking = getBookingByIdUseCase.getBookingById(entityId);

                if (Objects.equals(paymentStatus, "paid") && booking.getBookingStatus() == BookingStatus.Unpaid) {
                    updateBookingStatusUseCase.setBookingPaymentId(entityId, paymentId);
                    updateBookingStatusUseCase.updateBookingStatus(entityId, BookingStatus.Paid);

                    PaymentEntity paymentEntity = createPaymentUseCase.createPayment(CreatePaymentRequest.builder()
                            .description(String.format("Car rental - %s -> %s", booking.getStartCity().getName(), booking.getEndCity().getName()))
                            .totalCost(booking.getTotalPrice())
                            .userId(booking.getUser().getId())
                            .build());

                    setPaymentToPaid.setPaymentToPaid((paymentEntity.getId()));

                    scheduleBookingJobsUseCase.scheduleStartAndEndJobs(booking.getId(), booking.getStartDateTime(), booking.getEndDateTime());

                    return ResponseEntity.accepted().build();
                } else {
                    if(booking.getBookingStatus() == BookingStatus.Unpaid){
                        updateBookingStatusUseCase.updateBookingStatus(entityId, BookingStatus.Canceled);
                        return ResponseEntity.badRequest().build();
                    }

                    return ResponseEntity.accepted().build();
                }
            }else if(paymentType.equals("payment")){
                if (!Objects.equals(paymentStatus, "paid")) return ResponseEntity.badRequest().build();

                setPaymentToPaid.setPaymentToPaid(entityId);

                return ResponseEntity.accepted().build();
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/cancel")
    public ResponseEntity<Void> cancel(@RequestParam("bookingId") Long bookingId) {
        updateBookingStatusUseCase.updateBookingStatus(bookingId, BookingStatus.Canceled);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pay")
    public ResponseEntity<String> createCheckoutSession(@RequestParam("bookingId") Long bookingId) {
        Booking booking = getBookingByIdUseCase.getBookingById(bookingId);

        String paymentDescription = String.format("Booking - (%s) -> (%s)", booking.getStartCity().getName(), booking.getEndCity().getName());
        try {
            String url = this.createPaymentSessionUseCase.createPaymentSession(paymentDescription, booking.getTotalPrice(), "booking", bookingId);

            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

}