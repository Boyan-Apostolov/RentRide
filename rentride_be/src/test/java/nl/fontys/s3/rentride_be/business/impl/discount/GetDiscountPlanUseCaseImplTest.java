package nl.fontys.s3.rentride_be.business.impl.discount;

import nl.fontys.s3.rentride_be.domain.discount.DiscountPlan;
import nl.fontys.s3.rentride_be.persistance.DiscountPlanRepository;
import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetDiscountPlanUseCaseImplTest {
    @Mock
    private DiscountPlanRepository discountPlanRepository;

    @InjectMocks
    private GetDiscountPlanUseCaseImpl getDiscountPlanUseCase;

    @Test
    void getDiscountPlan_shouldReturnDiscountPlanWhenFound() {
        Long planId = 1L;
        DiscountPlanEntity discountPlanEntity = DiscountPlanEntity.builder()
                .id(planId)
                .title("Plan A")
                .discountValue(10)
                .build();

        when(discountPlanRepository.findById(planId)).thenReturn(Optional.of(discountPlanEntity));

        DiscountPlan expectedPlan = DiscountPlan.builder()
                .id(planId)
                .title("Plan A")
                .discountValue(10)
                .build();

        DiscountPlan result = getDiscountPlanUseCase.getDiscountPlan(planId);

        assertThat(result).isEqualTo(expectedPlan);
        verify(discountPlanRepository).findById(planId);
    }

    @Test
    void getDiscountPlan_shouldReturnNullWhenNotFound() {
        Long planId = 1L;
        when(discountPlanRepository.findById(planId)).thenReturn(Optional.empty());

        DiscountPlan result = getDiscountPlanUseCase.getDiscountPlan(planId);

        assertThat(result).isNull();
        verify(discountPlanRepository).findById(planId);
    }
}