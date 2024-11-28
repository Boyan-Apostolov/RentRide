package nl.fontys.s3.rentride_be.business.impl.discount;

import nl.fontys.s3.rentride_be.configuration.security.token.AccessToken;
import nl.fontys.s3.rentride_be.domain.discount.IsDiscountPlanBoughtResponse;
import nl.fontys.s3.rentride_be.persistance.DiscountPlanPurchaseRepository;
import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanPurchaseEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class IsDiscountPlanBoughtUseCaseImplTest {

    @Mock
    private DiscountPlanPurchaseRepository discountPlanPurchaseRepository;

    @Mock
    private AccessToken accessToken;

    @InjectMocks
    private IsDiscountPlanBoughtUseCaseImpl isDiscountPlanBoughtUseCase;

    @Test
    void isDiscountPlanBought_shouldReturnTrue_whenPlanIsBought() {
        Long userId = 1L;
        Long discountPlanId = 101L;

        when(accessToken.getUserId()).thenReturn(userId);
        when(discountPlanPurchaseRepository.findByUserIdAndDiscountPlanId(userId, discountPlanId))
                .thenReturn(Optional.of(DiscountPlanPurchaseEntity.builder().build()));

        IsDiscountPlanBoughtResponse response = isDiscountPlanBoughtUseCase.isDiscountPlanBought(discountPlanId);

        assertThat(response.isBought()).isTrue();
        verify(accessToken).getUserId();
        verify(discountPlanPurchaseRepository).findByUserIdAndDiscountPlanId(userId, discountPlanId);
    }

    @Test
    void isDiscountPlanBought_shouldReturnFalse_whenPlanIsNotBought() {
        Long userId = 1L;
        Long discountPlanId = 101L;

        when(accessToken.getUserId()).thenReturn(userId);
        when(discountPlanPurchaseRepository.findByUserIdAndDiscountPlanId(userId, discountPlanId))
                .thenReturn(Optional.empty());

        IsDiscountPlanBoughtResponse response = isDiscountPlanBoughtUseCase.isDiscountPlanBought(discountPlanId);

        assertThat(response.isBought()).isFalse();
        verify(accessToken).getUserId();
        verify(discountPlanPurchaseRepository).findByUserIdAndDiscountPlanId(userId, discountPlanId);
    }

    @Test
    void isDiscountPlanBought_shouldHandleNullAccessToken() {
        Long discountPlanId = 101L;

        when(accessToken.getUserId()).thenReturn(null);

        IsDiscountPlanBoughtResponse response = isDiscountPlanBoughtUseCase.isDiscountPlanBought(discountPlanId);

        assertThat(response.isBought()).isFalse();
        verify(accessToken).getUserId();
    }
}