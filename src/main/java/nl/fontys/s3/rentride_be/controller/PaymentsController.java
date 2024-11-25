package nl.fontys.s3.rentride_be.controller;

import com.stripe.model.checkout.Session;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.booking.GetBookingByIdUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.booking.ScheduleBookingJobsUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.booking.UpdateBookingStatusUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.discount.DeleteDiscountPlanPurchaseUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.discount.GetDiscountPlanPurchaseUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.discount.GetDiscountPlanPurchasesByUser;
import nl.fontys.s3.rentride_be.business.use_cases.discount.UpdateDiscountPlanPurchaseUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.payment.*;
import nl.fontys.s3.rentride_be.domain.booking.Booking;
import nl.fontys.s3.rentride_be.domain.discount.CreateDiscountPaymentRequest;
import nl.fontys.s3.rentride_be.domain.discount.DiscountPlanPurchase;
import nl.fontys.s3.rentride_be.domain.discount.UpdateDiscountPaymentRequest;
import nl.fontys.s3.rentride_be.domain.payment.CreatePaymentRequest;
import nl.fontys.s3.rentride_be.domain.payment.Payment;
import nl.fontys.s3.rentride_be.persistance.entity.BookingStatus;
import nl.fontys.s3.rentride_be.persistance.entity.PaymentEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    private GetPaymentsUseCase getPaymentsUseCase;
    private SetPaymentToPaid setPaymentToPaid;
    private GetDiscountPlanPurchaseUseCase getDiscountPlanPurchaseUseCase;
    private UpdateDiscountPlanPurchaseUseCase updateDiscountPlanPurchaseUseCase;
    private DeleteDiscountPlanPurchaseUseCase deleteDiscountPlanPurchaseUseCase;
    private GetDiscountPlanPurchasesByUser getDiscountPlanPurchasesByUser;

    private ScheduleBookingJobsUseCase scheduleBookingJobsUseCase;

    @GetMapping()
    public ResponseEntity<List<Payment>> getPayments() {
        List<Payment> userPayments = getPaymentsUseCase.getPayments();

        return ResponseEntity.ok(userPayments);
    }

    @GetMapping("by-user")
    public ResponseEntity<List<Payment>> getPaymentsByUser() {
        List<Payment> userPayments = getPaymentsByUser.getPaymentsBySessionUser();

        return ResponseEntity.ok(userPayments);
    }

    @PostMapping("create-discount-link")
    public ResponseEntity<String> createPaymentRequest(@RequestBody @Valid CreateDiscountPaymentRequest request) {
        DiscountPlanPurchase discountPlanPurchaseEntity = getDiscountPlanPurchaseUseCase.getDiscountPlanPurchaseByCurrentUserAndDiscountId(request.getDiscountPlanId());

        String paymentDescription = String.format("Discount Plan: %s", discountPlanPurchaseEntity.getDiscountPlan().getTitle());
        try {
            String url = this.createPaymentSessionUseCase.createPaymentSession(paymentDescription, discountPlanPurchaseEntity.getDiscountPlan().getPrice(), "discount", request.getDiscountPlanId());

            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment cannot be created");
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

            if (paymentType.equals("booking")) {
                return handlePaymentSuccessForBooking(entityId, paymentStatus, paymentId);
            } else if (paymentType.equals("payment")) {
                if (!Objects.equals(paymentStatus, "paid"))
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

                setPaymentToPaid.setPaymentToPaid(entityId);

                return ResponseEntity.accepted().build();
            } else if (paymentType.equals("discount")) {
                return handlePaymentSuccessForDiscountPlan(entityId, paymentStatus);
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.badRequest().build();
    }

    private ResponseEntity<String> handlePaymentSuccessForDiscountPlan(Long entityId, String paymentStatus) {
        DiscountPlanPurchase discountPlanPurchase = getDiscountPlanPurchaseUseCase.getDiscountPlanPurchaseByCurrentUserAndDiscountId(entityId);

        if (!Objects.equals(paymentStatus, "paid")) {
            deleteDiscountPlanPurchaseUseCase.deleteDiscountPlanPurchase(discountPlanPurchase.getUser().getId(), discountPlanPurchase.getDiscountPlan().getId());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        PaymentEntity paymentEntity = createPaymentUseCase.createPayment(CreatePaymentRequest.builder()
                .description(String.format("Discount Plan: %s", discountPlanPurchase.getDiscountPlan().getTitle()))
                .totalCost(discountPlanPurchase.getDiscountPlan().getPrice())
                .userId(discountPlanPurchase.getUser().getId())
                .build());

        setPaymentToPaid.setPaymentToPaid((paymentEntity.getId()));

        updateDiscountPlanPurchaseUseCase.updateDiscountPlanPurchase(UpdateDiscountPaymentRequest.builder()
                .discountPlanId(discountPlanPurchase.getDiscountPlan().getId())
                .remainingUses(discountPlanPurchase.getDiscountPlan().getRemainingUses())
                .build());

        return ResponseEntity.accepted().build();
    }

    private ResponseEntity<String> handlePaymentSuccessForBooking(Long entityId, String paymentStatus, String paymentId) {
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

            List<DiscountPlanPurchase> purchasedPlans = getDiscountPlanPurchasesByUser.getDiscountPlanPurchasesByUser(null);
            Optional<DiscountPlanPurchase> currentPlanOptional = purchasedPlans.stream().findFirst();
            if (currentPlanOptional.isPresent()) {
                DiscountPlanPurchase currentPlan = currentPlanOptional.get();

                updateDiscountPlanPurchaseUseCase.updateDiscountPlanPurchase(UpdateDiscountPaymentRequest.builder()
                                .discountPlanId(currentPlan.getDiscountPlan().getId())
                                .remainingUses(currentPlan.getRemainingUses() - 1)
                        .build());
            }

            scheduleBookingJobsUseCase.scheduleStartAndEndJobs(booking.getId(), booking.getStartDateTime(), booking.getEndDateTime());

            return ResponseEntity.accepted().build();
        } else {
            if (booking.getBookingStatus() == BookingStatus.Unpaid) {
                updateBookingStatusUseCase.updateBookingStatus(entityId, BookingStatus.Canceled);

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment cancelled");
            }

            return ResponseEntity.accepted().build();
        }
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment cannot be created");
        }
    }

}