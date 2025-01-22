package nl.fontys.s3.rentride_be.business.impl.discount;

import nl.fontys.s3.rentride_be.domain.discount.DiscountPlan;
import nl.fontys.s3.rentride_be.persistance.DiscountPlanRepository;
import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllDiscountPlansUseCaseImplTest {

    @Mock
    private DiscountPlanRepository discountPlanRepository;

    @InjectMocks
    private GetAllDiscountPlansUseCaseImpl getAllDiscountPlansUseCase;

    private DiscountPlanEntity discountPlanEntity1;
    private DiscountPlanEntity discountPlanEntity2;
    private DiscountPlan discountPlan1;
    private DiscountPlan discountPlan2;

    @BeforeEach
    void setUp() {
        discountPlanEntity1 = DiscountPlanEntity.builder()
                .id(1L)
                .title("Plan 1")
                .description("Description 1")
                .price(100.0)
                .remainingUses(5)
                .discountValue(10)
                .build();

        discountPlanEntity2 = DiscountPlanEntity.builder()
                .id(2L)
                .title("Plan 2")
                .description("Description 2")
                .price(200.0)
                .remainingUses(10)
                .discountValue(15)
                .build();

        discountPlan1 = DiscountPlanConverter.convert(discountPlanEntity1);
        discountPlan2 = DiscountPlanConverter.convert(discountPlanEntity2);
    }

    @Test
    void getAllDiscountPlans_ShouldReturnAllDiscountPlans() {
        when(discountPlanRepository.findAll()).thenReturn(List.of(discountPlanEntity1, discountPlanEntity2));

        List<DiscountPlan> result = getAllDiscountPlansUseCase.getAllDiscountPlans();

        assertEquals(2, result.size());
        assertEquals(discountPlan1, result.get(0));
        assertEquals(discountPlan2, result.get(1));

        verify(discountPlanRepository, times(1)).findAll();
    }

    @Test
    void getAllDiscountPlans_ShouldReturnEmptyList_WhenNoPlansExist() {
        when(discountPlanRepository.findAll()).thenReturn(List.of());

        List<DiscountPlan> result = getAllDiscountPlansUseCase.getAllDiscountPlans();

        assertEquals(0, result.size());

        verify(discountPlanRepository, times(1)).findAll();
    }
}