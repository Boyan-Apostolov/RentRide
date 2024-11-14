package nl.fontys.s3.rentride_be.business.impl.discount;

import nl.fontys.s3.rentride_be.domain.discount.CreateDiscountPlanRequest;
import nl.fontys.s3.rentride_be.domain.discount.CreateDiscountPlanResponse;
import nl.fontys.s3.rentride_be.persistance.DiscountPlanRepository;
import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateDiscountPlanUseCaseImplTest {

    @Mock
    private DiscountPlanRepository discountPlanRepository;

    @InjectMocks
    private CreateDiscountPlanUseCaseImpl createDiscountPlanUseCase;

    private CreateDiscountPlanRequest request;
    private DiscountPlanEntity savedEntity;

    @BeforeEach
    void setUp() {
        request = new CreateDiscountPlanRequest();
        request.setTitle("Winter Sale");
        request.setDiscountValue(15);
        request.setDescription("15% off for winter bookings");
        request.setRemainingUses(10);
        request.setPrice(50.0);

        savedEntity = DiscountPlanEntity.builder()
                .id(1L)
                .title(request.getTitle())
                .discountValue(request.getDiscountValue())
                .description(request.getDescription())
                .remainingUses(request.getRemainingUses())
                .price(request.getPrice())
                .build();
    }

    @Test
    void createDiscountPlan_ShouldReturnResponseWithId_WhenDiscountPlanIsSaved() {
        when(discountPlanRepository.save(any(DiscountPlanEntity.class))).thenReturn(savedEntity);

        CreateDiscountPlanResponse response = createDiscountPlanUseCase.createDiscountPlan(request);

        assertEquals(1L, response.getDiscountPlanId());
        verify(discountPlanRepository, times(1)).save(argThat(discountPlan ->
                discountPlan.getTitle().equals(request.getTitle()) &&
                        discountPlan.getDiscountValue() == request.getDiscountValue() &&
                        discountPlan.getDescription().equals(request.getDescription()) &&
                        discountPlan.getRemainingUses() == request.getRemainingUses() &&
                        discountPlan.getPrice() == request.getPrice()
        ));
    }
}