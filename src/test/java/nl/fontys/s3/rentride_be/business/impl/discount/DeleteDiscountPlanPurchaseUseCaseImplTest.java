package nl.fontys.s3.rentride_be.business.impl.discount;

import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.persistance.DiscountPlanPurchaseRepository;
import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanPurchaseEntity;
import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanPurchaseKey;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteDiscountPlanPurchaseUseCaseImplTest {

    @Mock
    private DiscountPlanPurchaseRepository purchaseRepository;

    @InjectMocks
    private DeleteDiscountPlanPurchaseUseCaseImpl deleteDiscountPlanPurchaseUseCase;

    @Test
    void deleteDiscountPlanPurchase_ShouldDeleteSuccessfully_WhenEntityExists() {
        Long userId = 1L;
        Long discountPlanId = 2L;

        DiscountPlanPurchaseEntity purchaseEntity = new DiscountPlanPurchaseEntity();
        DiscountPlanPurchaseKey key = new DiscountPlanPurchaseKey(userId, discountPlanId);

        when(purchaseRepository.findByUserIdAndDiscountPlanId(userId, discountPlanId))
                .thenReturn(Optional.of(purchaseEntity));

        deleteDiscountPlanPurchaseUseCase.deleteDiscountPlanPurchase(userId, discountPlanId);

        verify(purchaseRepository, times(1)).deleteById(key);
    }

    @Test
    void deleteDiscountPlanPurchase_ShouldThrowNotFoundException_WhenEntityDoesNotExist() {
        Long userId = 1L;
        Long discountPlanId = 2L;

        when(purchaseRepository.findByUserIdAndDiscountPlanId(userId, discountPlanId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                deleteDiscountPlanPurchaseUseCase.deleteDiscountPlanPurchase(userId, discountPlanId));

        verify(purchaseRepository, never()).deleteById(any());
    }
}