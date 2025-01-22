package nl.fontys.s3.rentride_be.controller;

import nl.fontys.s3.rentride_be.business.use_cases.discount.*;
import nl.fontys.s3.rentride_be.domain.discount.CreateDiscountPaymentRequest;
import nl.fontys.s3.rentride_be.domain.discount.CreateDiscountPlanRequest;
import nl.fontys.s3.rentride_be.domain.discount.CreateDiscountPlanResponse;
import nl.fontys.s3.rentride_be.domain.discount.DiscountPlan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiscountPlansControllerTest {

    @Mock
    private GetAllDiscountPlansUseCase getAllDiscountPlansUseCase;

    @Mock
    private DeleteDiscountPlanUseCase deleteDiscountPlanUseCase;

    @Mock
    private CreateDiscountPlanUseCase createDiscountPlanUseCase;

    @Mock
    private CreateDiscountPlanPurchaseUseCase createDiscountPlanPurchaseUseCase;

    @InjectMocks
    private DiscountPlansController discountPlansController;

    private DiscountPlan discountPlan;
    private CreateDiscountPlanRequest createDiscountPlanRequest;
    private CreateDiscountPlanResponse createDiscountPlanResponse;
    private CreateDiscountPaymentRequest createDiscountPaymentRequest;

    @BeforeEach
    void setUp() {
        discountPlan = DiscountPlan.builder()
                .id(1L)
                .title("Test Plan")
                .description("Test Description")
                .remainingUses(10)
                .price(100.0)
                .discountValue(15)
                .build();

        createDiscountPlanRequest = CreateDiscountPlanRequest.builder()
                .title("New Plan")
                .description("New Description")
                .remainingUses(5)
                .price(50.0)
                .discountValue(10)
                .build();

        createDiscountPlanResponse = CreateDiscountPlanResponse.builder()
                .discountPlanId(2L)
                .build();

        createDiscountPaymentRequest = CreateDiscountPaymentRequest.builder()
                .discountPlanId(1L)
                .build();
    }

    @Test
    void getAllDiscountPlans_ShouldReturnListOfDiscountPlans() {
        when(getAllDiscountPlansUseCase.getAllDiscountPlans()).thenReturn(List.of(discountPlan));

        ResponseEntity<List<DiscountPlan>> response = discountPlansController.getAllDiscountPlans();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(discountPlan, response.getBody().get(0));

        verify(getAllDiscountPlansUseCase, times(1)).getAllDiscountPlans();
    }

    @Test
    void deleteDiscountPlan_ShouldReturnNoContent() {
        Long discountPlanId = 1L;

        ResponseEntity<String> response = discountPlansController.deleteDiscountPlan(discountPlanId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(deleteDiscountPlanUseCase, times(1)).deleteDiscountPlan(discountPlanId);
    }

    @Test
    void createDiscountPlan_ShouldReturnCreatedResponse() {
        when(createDiscountPlanUseCase.createDiscountPlan(createDiscountPlanRequest)).thenReturn(createDiscountPlanResponse);

        ResponseEntity<CreateDiscountPlanResponse> response = discountPlansController.createDiscountPlan(createDiscountPlanRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createDiscountPlanResponse, response.getBody());

        verify(createDiscountPlanUseCase, times(1)).createDiscountPlan(createDiscountPlanRequest);
    }

    @Test
    void createDiscountPlanPurchase_ShouldReturnOk() {
        ResponseEntity<CreateDiscountPlanResponse> response = discountPlansController.createDiscountPlanPurchase(createDiscountPaymentRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(createDiscountPlanPurchaseUseCase, times(1)).createDiscountPlanPurchase(createDiscountPaymentRequest);
    }
}