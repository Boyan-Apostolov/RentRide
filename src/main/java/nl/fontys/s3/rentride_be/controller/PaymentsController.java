package nl.fontys.s3.rentride_be.controller;

import com.stripe.model.checkout.Session;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.impl.discount.CreateDiscountPlanPurchaseUseCaseImpl;
import nl.fontys.s3.rentride_be.business.use_cases.auction.GetAuctionUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.auction.UpdateAuctionCanBeClaimedUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.auth.EmailerUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.booking.GetBookingByIdUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.booking.ScheduleBookingJobsUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.booking.UpdateBookingStatusUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.discount.*;
import nl.fontys.s3.rentride_be.business.use_cases.payment.*;
import nl.fontys.s3.rentride_be.domain.auction.Auction;
import nl.fontys.s3.rentride_be.domain.booking.Booking;
import nl.fontys.s3.rentride_be.domain.discount.CreateDiscountPaymentRequest;
import nl.fontys.s3.rentride_be.domain.discount.DiscountPlan;
import nl.fontys.s3.rentride_be.domain.discount.DiscountPlanPurchase;
import nl.fontys.s3.rentride_be.domain.discount.UpdateDiscountPaymentRequest;
import nl.fontys.s3.rentride_be.domain.payment.CreatePaymentRequest;
import nl.fontys.s3.rentride_be.domain.payment.Payment;
import nl.fontys.s3.rentride_be.domain.user.EmailType;
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

    private GetDiscountPlanUseCase getDiscountPlanUseCase;
    private CreateDiscountPlanPurchaseUseCaseImpl createDiscountPlanPurchaseUseCaseImpl;
    private CreatePaymentSessionUseCase createPaymentSessionUseCase;
    private GetBookingByIdUseCase getBookingByIdUseCase;
    private UpdateBookingStatusUseCase updateBookingStatusUseCase;
    private CreatePaymentUseCase createPaymentUseCase;
    private UpdatePaymentUseCase updatePaymentUseCase;
    private GetPaymentsByUser getPaymentsByUser;
    private GetPaymentsUseCase getPaymentsUseCase;
    private UpdatePaymentStatusUseCase updatePaymentStatusUseCase;
    private GetDiscountPlanPurchaseUseCase getDiscountPlanPurchaseUseCase;
    private UpdateDiscountPlanPurchaseUseCase updateDiscountPlanPurchaseUseCase;
    private DeleteDiscountPlanPurchaseUseCase deleteDiscountPlanPurchaseUseCase;
    private GetDiscountPlanPurchasesByUser getDiscountPlanPurchasesByUser;
    private GetAuctionUseCase getAuctionUseCase;
    private UpdateAuctionCanBeClaimedUseCase updateAuctionCanBeClaimedUseCase;
    private ScheduleBookingJobsUseCase scheduleBookingJobsUseCase;
    private EmailerUseCase emailerUseCase;

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
        DiscountPlan discountPlan = getDiscountPlanUseCase.getDiscountPlan(request.getDiscountPlanId());

        String paymentDescription = String.format("Discount Plan: %s", discountPlan.getTitle());
        try {
            String url = this.createPaymentSessionUseCase.createPaymentSession(paymentDescription, discountPlan.getPrice(), "discount", request.getDiscountPlanId());

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
            boolean isPaid = Objects.equals(paymentStatus, "paid");

            if (paymentType.equals("booking")) {
                return handlePaymentSuccessForBooking(entityId, paymentStatus, paymentId);
            } else if (paymentType.equals("payment")) {
                if (!isPaid)
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

                updatePaymentStatusUseCase.updatePaymentStatus(entityId, isPaid);

                return ResponseEntity.accepted().build();
            } else if (paymentType.equals("discount")) {
                return handlePaymentSuccessForDiscountPlan(entityId, paymentStatus);
            } else if (paymentType.equals("auction")) {
                return handlePaymentSuccessForAuction(entityId, paymentStatus);
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.badRequest().build();
    }

    private ResponseEntity<String> handlePaymentSuccessForAuction(Long entityId, String paymentStatus) {
        Auction auction = getAuctionUseCase.getAuction(entityId);

        if (Objects.equals(paymentStatus, "paid") && auction.getWinnerUser() != null) {
            updateAuctionCanBeClaimedUseCase.updateState(entityId, 1);

            emailerUseCase.send(auction.getWinnerUser().getEmail(), "Auction can be claimed!",
                    String.format("You have successfully paid you winning bid for auction #%s and can now create a booking with the winning car from the auction page!",
                            auction.getId()),
                    EmailType.BOOKING);

            return ResponseEntity.accepted().build();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment cancelled");
    }

    private ResponseEntity<String> handlePaymentSuccessForDiscountPlan(Long entityId, String paymentStatus) {
        if (!Objects.equals(paymentStatus, "paid")) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        DiscountPlanPurchase purchase = createDiscountPlanPurchaseUseCaseImpl.createDiscountPlanPurchase(CreateDiscountPaymentRequest.builder().discountPlanId(entityId).build());

        PaymentEntity paymentEntity = createPaymentUseCase.createPayment(CreatePaymentRequest.builder()
                .description(String.format("Discount Plan: %s", purchase.getDiscountPlan().getTitle()))
                .totalCost(purchase.getDiscountPlan().getPrice())
                .userId(purchase.getUser().getId())
                .build());

        updatePaymentStatusUseCase.updatePaymentStatus((paymentEntity.getId()), true);

        updateDiscountPlanPurchaseUseCase.updateDiscountPlanPurchase(UpdateDiscountPaymentRequest.builder()
                .discountPlanId(purchase.getDiscountPlan().getId())
                .remainingUses(purchase.getDiscountPlan().getRemainingUses())
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

            updatePaymentStatusUseCase.updatePaymentStatus((paymentEntity.getId()), true);

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

            emailerUseCase.send(booking.getUser().getEmail(), "Booking reserved!",
                    String.format("Your booking with number #%s from %s to %s has been successfully registered! You will receive a second confirmation email when the booking starts!",
                            booking.getId(), booking.getStartCity().getName(), booking.getEndCity().getName()),
                    EmailType.BOOKING);

            return ResponseEntity.accepted().build();
        } else {
            if (booking.getBookingStatus() == BookingStatus.Unpaid) {
                updateBookingStatusUseCase.updateBookingStatus(entityId, BookingStatus.Canceled);

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment cancelled");
            }

            return ResponseEntity.accepted().build();
        }
    }

    private ResponseEntity<String> handlePaymentFailedForBooking(Long entityId, String paymentStatus, String paymentId) {
        Booking booking = getBookingByIdUseCase.getBookingById(entityId);

        if (!Objects.equals(paymentStatus, "paid") && booking.getBookingStatus() == BookingStatus.Unpaid) {
            updateBookingStatusUseCase.setBookingPaymentId(entityId, paymentId);

            PaymentEntity paymentEntity = createPaymentUseCase.createPayment(CreatePaymentRequest.builder()
                    .description(String.format("Car rental - %s -> %s", booking.getStartCity().getName(), booking.getEndCity().getName()))
                    .totalCost(booking.getTotalPrice())
                    .userId(booking.getUser().getId())
                    .build());

            updatePaymentStatusUseCase.updatePaymentStatus((paymentEntity.getId()), false);

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
    public ResponseEntity<String> cancel(@RequestParam("sessionId") String sessionId,
                                         @RequestParam("paymentType") String paymentType,
                                         @RequestParam("entityId") Long entityId) {

        try {
            Session session = Session.retrieve(sessionId);
            String paymentStatus = session.getPaymentStatus();
            String paymentId = session.getPaymentIntent();

            if (Objects.equals(paymentStatus, "paid")) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

            if (paymentType.equals("booking")) {
                return handlePaymentFailedForBooking(entityId, paymentStatus, paymentId);
            } else if (paymentType.equals("payment")) {

                updatePaymentStatusUseCase.updatePaymentStatus(entityId, false);

                return ResponseEntity.accepted().build();
            } else if (paymentType.equals("discount")) {
                return ResponseEntity.ok().build();
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

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