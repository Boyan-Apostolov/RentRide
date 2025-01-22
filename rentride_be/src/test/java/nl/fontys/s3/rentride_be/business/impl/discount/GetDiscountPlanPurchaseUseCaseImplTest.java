package nl.fontys.s3.rentride_be.business.impl.discount;

import nl.fontys.s3.rentride_be.configuration.security.token.AccessToken;
import nl.fontys.s3.rentride_be.domain.discount.DiscountPlanPurchase;
import nl.fontys.s3.rentride_be.persistance.DiscountPlanPurchaseRepository;
import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanEntity;
import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanPurchaseEntity;
import nl.fontys.s3.rentride_be.persistance.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetDiscountPlanPurchaseUseCaseImplTest {

    @Mock
    private DiscountPlanPurchaseRepository discountPlanPurchaseRepository;

    @Mock
    private AccessToken requestAccessToken;

    @InjectMocks
    private GetDiscountPlanPurchaseUseCaseImpl getDiscountPlanPurchaseUseCase;

    private DiscountPlanPurchaseEntity discountPlanPurchaseEntity;
    private DiscountPlanEntity discountPlanEntity;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        discountPlanEntity = DiscountPlanEntity.builder()
                .id(1L)
                .title("Test Plan")
                .discountValue(10)
                .price(50.0)
                .remainingUses(5)
                .build();

        userEntity = UserEntity.builder()
                .id(2L)
                .email("test@example.com")
                .build();

        discountPlanPurchaseEntity = DiscountPlanPurchaseEntity.builder()
                .id(null) // Assuming no composite key for simplicity
                .user(userEntity)
                .discountPlan(discountPlanEntity)
                .purchaseDate(LocalDateTime.now())
                .remainingUses(3)
                .build();
    }

    @Test
    void getDiscountPlanPurchaseByCurrentUserAndDiscountId_ShouldReturnPurchase_WhenExists() {
        Long discountId = 1L;
        Long currentUserId = 2L;

        when(requestAccessToken.getUserId()).thenReturn(currentUserId);
        when(discountPlanPurchaseRepository.findByUserIdAndDiscountPlanId(currentUserId, discountId))
                .thenReturn(Optional.of(discountPlanPurchaseEntity));

        DiscountPlanPurchase result = getDiscountPlanPurchaseUseCase.getDiscountPlanPurchaseByCurrentUserAndDiscountId(discountId);

        assertNotNull(result);
        assertEquals(discountPlanEntity.getId(), result.getDiscountPlan().getId());
        assertEquals(userEntity.getId(), result.getUser().getId());
        assertEquals(discountPlanPurchaseEntity.getRemainingUses(), result.getRemainingUses());

        verify(requestAccessToken, times(1)).getUserId();
        verify(discountPlanPurchaseRepository, times(1)).findByUserIdAndDiscountPlanId(currentUserId, discountId);
    }

    @Test
    void getDiscountPlanPurchaseByCurrentUserAndDiscountId_ShouldReturnNull_WhenNotExists() {
        Long discountId = 1L;
        Long currentUserId = 2L;

        when(requestAccessToken.getUserId()).thenReturn(currentUserId);
        when(discountPlanPurchaseRepository.findByUserIdAndDiscountPlanId(currentUserId, discountId))
                .thenReturn(Optional.empty());

        DiscountPlanPurchase result = getDiscountPlanPurchaseUseCase.getDiscountPlanPurchaseByCurrentUserAndDiscountId(discountId);

        assertNull(result);

        verify(requestAccessToken, times(1)).getUserId();
        verify(discountPlanPurchaseRepository, times(1)).findByUserIdAndDiscountPlanId(currentUserId, discountId);
    }
}