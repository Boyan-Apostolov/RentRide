package nl.fontys.s3.rentride_be.business.impl.discount;

import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.domain.discount.CreateDiscountPaymentRequest;
import nl.fontys.s3.rentride_be.persistance.DiscountPlanPurchaseRepository;
import nl.fontys.s3.rentride_be.persistance.DiscountPlanRepository;
import nl.fontys.s3.rentride_be.persistance.UserRepository;
import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanEntity;
import nl.fontys.s3.rentride_be.persistance.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateDiscountPlanPurchaseUseCaseImplTest {

    @Mock
    private DiscountPlanPurchaseRepository discountPlanPurchaseRepository;

    @Mock
    private DiscountPlanRepository discountPlanRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateDiscountPlanPurchaseUseCaseImpl createDiscountPlanPurchaseUseCase;

    private CreateDiscountPaymentRequest request;
    private DiscountPlanEntity mockDiscountPlan;
    private UserEntity mockUser;

    @BeforeEach
    void setUp() {
        request = new CreateDiscountPaymentRequest();
        request.setDiscountPlanId(1L);

        mockDiscountPlan = new DiscountPlanEntity();
        mockDiscountPlan.setId(1L);

        mockUser = new UserEntity();
        mockUser.setId(1L);
    }

    @Test
    void createDiscountPlanPurchase_ShouldSavePurchase_WhenEntitiesExist() {
        when(discountPlanRepository.findById(request.getDiscountPlanId())).thenReturn(Optional.of(mockDiscountPlan));
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        createDiscountPlanPurchaseUseCase.createDiscountPlanPurchase(request);

        verify(discountPlanPurchaseRepository, times(1)).save(argThat(purchase ->
                purchase.getDiscountPlan().equals(mockDiscountPlan) &&
                        purchase.getUser().equals(mockUser) &&
                        purchase.getRemainingUses() == 0 &&
                        purchase.getId().getDiscountPlanId().equals(mockDiscountPlan.getId()) &&
                        purchase.getId().getUserId().equals(mockUser.getId()) &&
                        purchase.getPurchaseDate() != null
        ));
    }

    @Test
    void createDiscountPlanPurchase_ShouldThrowNotFoundException_WhenDiscountPlanNotFound() {
        when(discountPlanRepository.findById(request.getDiscountPlanId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> createDiscountPlanPurchaseUseCase.createDiscountPlanPurchase(request));
    }

    @Test
    void createDiscountPlanPurchase_ShouldThrowNotFoundException_WhenUserNotFound() {
        when(discountPlanRepository.findById(request.getDiscountPlanId())).thenReturn(Optional.of(mockDiscountPlan));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> createDiscountPlanPurchaseUseCase.createDiscountPlanPurchase(request));
    }
}