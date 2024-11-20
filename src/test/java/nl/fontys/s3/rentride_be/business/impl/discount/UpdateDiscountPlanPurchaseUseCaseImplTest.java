package nl.fontys.s3.rentride_be.business.impl.discount;

import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.use_cases.discount.DeleteDiscountPlanPurchaseUseCase;
import nl.fontys.s3.rentride_be.configuration.security.token.AccessToken;
import nl.fontys.s3.rentride_be.domain.discount.UpdateDiscountPaymentRequest;
import nl.fontys.s3.rentride_be.persistance.DiscountPlanPurchaseRepository;
import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanPurchaseEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateDiscountPlanPurchaseUseCaseImplTest {

    @Mock
    private DiscountPlanPurchaseRepository discountPlanPurchaseRepository;

    @Mock
    private DeleteDiscountPlanPurchaseUseCase deleteDiscountPlanPurchaseUseCase;

    @InjectMocks
    private UpdateDiscountPlanPurchaseUseCaseImpl updateDiscountPlanPurchaseUseCase;

    @Mock
    private AccessToken accessToken;

    @BeforeEach
    void setUp() {
        when(accessToken.getUserId()).thenReturn(1L);
    }


    @Test
    void updateDiscountPlanPurchase_ShouldUpdateRemainingUses_WhenRemainingUsesIsGreaterThanZero() {
        Long discountPlanId = 1L;
        Long currentUserId = 1L;
        int newRemainingUses = 5;

        DiscountPlanPurchaseEntity discountPlanPurchaseEntity = new DiscountPlanPurchaseEntity();
        discountPlanPurchaseEntity.setRemainingUses(10);

        UpdateDiscountPaymentRequest request = UpdateDiscountPaymentRequest.builder()
                .discountPlanId(discountPlanId)
                .remainingUses(newRemainingUses)
                .build();

        when(discountPlanPurchaseRepository.findByUserIdAndDiscountPlanId(currentUserId, discountPlanId))
                .thenReturn(Optional.of(discountPlanPurchaseEntity));

        updateDiscountPlanPurchaseUseCase.updateDiscountPlanPurchase(request);

        verify(discountPlanPurchaseRepository, times(1)).save(discountPlanPurchaseEntity);
        assertEquals(newRemainingUses, discountPlanPurchaseEntity.getRemainingUses());
    }

    @Test
    void updateDiscountPlanPurchase_ShouldDeletePurchase_WhenRemainingUsesIsZero() {
        Long discountPlanId = 1L;
        Long currentUserId = 1L;

        DiscountPlanPurchaseEntity discountPlanPurchaseEntity = new DiscountPlanPurchaseEntity();
        discountPlanPurchaseEntity.setRemainingUses(10);

        UpdateDiscountPaymentRequest request = UpdateDiscountPaymentRequest.builder()
                .discountPlanId(discountPlanId)
                .remainingUses(0)
                .build();

        when(discountPlanPurchaseRepository.findByUserIdAndDiscountPlanId(currentUserId, discountPlanId))
                .thenReturn(Optional.of(discountPlanPurchaseEntity));

        updateDiscountPlanPurchaseUseCase.updateDiscountPlanPurchase(request);

        verify(deleteDiscountPlanPurchaseUseCase, times(1)).deleteDiscountPlanPurchase(currentUserId, discountPlanId);
        verify(discountPlanPurchaseRepository, never()).save(any());
    }

    @Test
    void updateDiscountPlanPurchase_ShouldThrowNotFoundException_WhenDiscountPlanPurchaseDoesNotExist() {
        Long discountPlanId = 1L;
        Long currentUserId = 1L;

        UpdateDiscountPaymentRequest request = UpdateDiscountPaymentRequest.builder()
                .discountPlanId(discountPlanId)
                .remainingUses(5)
                .build();

        when(discountPlanPurchaseRepository.findByUserIdAndDiscountPlanId(currentUserId, discountPlanId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> updateDiscountPlanPurchaseUseCase.updateDiscountPlanPurchase(request));

        verify(deleteDiscountPlanPurchaseUseCase, never()).deleteDiscountPlanPurchase(any(), any());
        verify(discountPlanPurchaseRepository, never()).save(any());
    }
}