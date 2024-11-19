package nl.fontys.s3.rentride_be.controller;

import com.stripe.model.checkout.Session;
import nl.fontys.s3.rentride_be.business.use_cases.booking.GetBookingByIdUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.booking.ScheduleBookingJobsUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.booking.UpdateBookingStatusUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.discount.DeleteDiscountPlanPurchaseUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.discount.GetDiscountPlanPurchaseUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.discount.GetDiscountPlanPurchasesByUser;
import nl.fontys.s3.rentride_be.business.use_cases.discount.UpdateDiscountPlanPurchaseUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.payment.*;
import nl.fontys.s3.rentride_be.domain.booking.Booking;
import nl.fontys.s3.rentride_be.domain.city.City;
import nl.fontys.s3.rentride_be.domain.discount.CreateDiscountPaymentRequest;
import nl.fontys.s3.rentride_be.domain.discount.DiscountPlan;
import nl.fontys.s3.rentride_be.domain.discount.DiscountPlanPurchase;
import nl.fontys.s3.rentride_be.domain.payment.CreatePaymentRequest;
import nl.fontys.s3.rentride_be.domain.payment.Payment;
import nl.fontys.s3.rentride_be.domain.user.User;
import nl.fontys.s3.rentride_be.persistance.entity.BookingStatus;
import nl.fontys.s3.rentride_be.persistance.entity.PaymentEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentsControllerTest {

    @Mock
    private CreatePaymentSessionUseCase createPaymentSessionUseCase;
    @Mock
    private GetBookingByIdUseCase getBookingByIdUseCase;
    @Mock
    private UpdateBookingStatusUseCase updateBookingStatusUseCase;
    @Mock
    private CreatePaymentUseCase createPaymentUseCase;
    @Mock
    private UpdatePaymentUseCase updatePaymentUseCase;
    @Mock
    private GetPaymentsByUser getPaymentsByUser;
    @Mock
    private GetPaymentsUseCase getPaymentsUseCase;
    @Mock
    private SetPaymentToPaid setPaymentToPaid;
    @Mock
    private GetDiscountPlanPurchaseUseCase getDiscountPlanPurchaseUseCase;
    @Mock
    private UpdateDiscountPlanPurchaseUseCase updateDiscountPlanPurchaseUseCase;
    @Mock
    private DeleteDiscountPlanPurchaseUseCase deleteDiscountPlanPurchaseUseCase;
    @Mock
    private GetDiscountPlanPurchasesByUser getDiscountPlanPurchasesByUser;
    @Mock
    private ScheduleBookingJobsUseCase scheduleBookingJobsUseCase;

    @InjectMocks
    private PaymentsController paymentsController;

    private Booking booking;
    private DiscountPlanPurchase discountPlanPurchase;
    private CreatePaymentRequest createPaymentRequest;
    private CreateDiscountPaymentRequest createDiscountPaymentRequest;
    private PaymentEntity paymentEntity;

    @BeforeEach
    void setUp() {
        booking = Booking.builder()
                .id(1L)
                .startCity(City.builder().name("City A").build())
                .endCity(City.builder().name("City B").build())
                .totalPrice(100.0)
                .bookingStatus(BookingStatus.Unpaid)
                .build();

        discountPlanPurchase = DiscountPlanPurchase.builder()
                .discountPlan(DiscountPlan.builder().title("Plan A").price(50.0).build())
                .user(User.builder().id(1L).build())
                .build();

        createPaymentRequest = CreatePaymentRequest.builder()
                .description("Payment for service")
                .totalCost(100.0)
                .build();

        createDiscountPaymentRequest = CreateDiscountPaymentRequest.builder()
                .discountPlanId(1L)
                .build();

        paymentEntity = PaymentEntity.builder().id(1L).build();
    }

    @Test
    void getPayments_ShouldReturnAllPayments() {
        List<Payment> payments = List.of(Payment.builder().id(1L).build());
        when(getPaymentsUseCase.getPayments()).thenReturn(payments);

        ResponseEntity<List<Payment>> response = paymentsController.getPayments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(payments, response.getBody());
        verify(getPaymentsUseCase, times(1)).getPayments();
    }

    @Test
    void createDiscountPaymentRequest_ShouldReturnPaymentUrl() throws Exception {
        when(getDiscountPlanPurchaseUseCase.getDiscountPlanPurchaseByCurrentUserAndDiscountId(1L)).thenReturn(discountPlanPurchase);
        when(createPaymentSessionUseCase.createPaymentSession(anyString(), anyDouble(), eq("discount"), eq(1L))).thenReturn("http://payment-url.com");

        ResponseEntity<String> response = paymentsController.createPaymentRequest(createDiscountPaymentRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("http://payment-url.com", response.getBody());
        verify(createPaymentSessionUseCase, times(1)).createPaymentSession(anyString(), anyDouble(), eq("discount"), eq(1L));
    }

    @Test
    void createPaymentRequest_ShouldReturnAccepted() throws Exception {
        when(createPaymentUseCase.createPayment(createPaymentRequest)).thenReturn(paymentEntity);
        when(createPaymentSessionUseCase.createPaymentSession(anyString(), anyDouble(), eq("payment"), eq(1L))).thenReturn("http://payment-url.com");

        ResponseEntity<String> response = paymentsController.createPaymentRequest(createPaymentRequest);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(updatePaymentUseCase, times(1)).updatePayment(paymentEntity);
    }
}