package nl.fontys.s3.rentride_be.business.impl.discount;

import nl.fontys.s3.rentride_be.configuration.security.token.AccessToken;
import nl.fontys.s3.rentride_be.domain.discount.DiscountPlanPurchase;
import nl.fontys.s3.rentride_be.persistance.DiscountPlanPurchaseRepository;
import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanPurchaseEntity;
import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanPurchaseKey;
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
class GetDiscountPlanPurchasesByUserImplTest {

    @Mock
    private DiscountPlanPurchaseRepository discountPlanPurchaseRepository;

    @Mock
    private AccessToken requestAccessToken;

    @InjectMocks
    private GetDiscountPlanPurchasesByUserImpl getDiscountPlanPurchasesByUser;

    private DiscountPlanPurchaseEntity discountPlanPurchaseEntity1;
    private DiscountPlanPurchaseEntity discountPlanPurchaseEntity2;

    @BeforeEach
    void setUp() {
        discountPlanPurchaseEntity1 = DiscountPlanPurchaseEntity.builder()
                .id(new DiscountPlanPurchaseKey(1L, 101L))
                .remainingUses(5)
                .build();

        discountPlanPurchaseEntity2 = DiscountPlanPurchaseEntity.builder()
                .id(new DiscountPlanPurchaseKey(2L, 102L))
                .remainingUses(3)
                .build();
    }

    @Test
    void getDiscountPlanPurchasesByUser_ShouldReturnPurchasesForProvidedUserId() {
        when(discountPlanPurchaseRepository.findAllByUserIdOrderByDiscountPlan_DiscountValueDesc(101L))
                .thenReturn(List.of(discountPlanPurchaseEntity1, discountPlanPurchaseEntity2));

        List<DiscountPlanPurchase> result = getDiscountPlanPurchasesByUser.getDiscountPlanPurchasesByUser(101L);

        assertEquals(2, result.size());
        verify(discountPlanPurchaseRepository, times(1)).findAllByUserIdOrderByDiscountPlan_DiscountValueDesc(101L);
    }

    @Test
    void getDiscountPlanPurchasesByUser_ShouldReturnPurchasesForCurrentUserId_WhenUserIdIsNull() {
        when(requestAccessToken.getUserId()).thenReturn(101L);
        when(discountPlanPurchaseRepository.findAllByUserIdOrderByDiscountPlan_DiscountValueDesc(101L))
                .thenReturn(List.of(discountPlanPurchaseEntity1, discountPlanPurchaseEntity2));

        List<DiscountPlanPurchase> result = getDiscountPlanPurchasesByUser.getDiscountPlanPurchasesByUser(null);

        assertEquals(2, result.size());
        verify(requestAccessToken, times(1)).getUserId();
        verify(discountPlanPurchaseRepository, times(1)).findAllByUserIdOrderByDiscountPlan_DiscountValueDesc(101L);
    }
}