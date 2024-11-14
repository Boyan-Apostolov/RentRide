package nl.fontys.s3.rentride_be.business.impl.discount;

import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.persistance.DiscountPlanRepository;
import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteDiscountPlanUseCaseImplTest {

    @Mock
    private DiscountPlanRepository discountPlanRepository;

    @InjectMocks
    private DeleteDiscountPlanUseCaseImpl deleteDiscountPlanUseCase;

    @Test
    void deleteDiscountPlan_ShouldDeleteSuccessfully_WhenEntityExists() {
        Long discountPlanId = 1L;
        DiscountPlanEntity discountPlanEntity = new DiscountPlanEntity();
        discountPlanEntity.setId(discountPlanId);

        when(discountPlanRepository.findById(discountPlanId))
                .thenReturn(Optional.of(discountPlanEntity));

        deleteDiscountPlanUseCase.deleteDiscountPlan(discountPlanId);

        verify(discountPlanRepository, times(1)).delete(discountPlanEntity);
    }

    @Test
    void deleteDiscountPlan_ShouldThrowNotFoundException_WhenEntityDoesNotExist() {
        Long discountPlanId = 1L;

        when(discountPlanRepository.findById(discountPlanId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                deleteDiscountPlanUseCase.deleteDiscountPlan(discountPlanId));

        verify(discountPlanRepository, never()).delete(any());
    }
}