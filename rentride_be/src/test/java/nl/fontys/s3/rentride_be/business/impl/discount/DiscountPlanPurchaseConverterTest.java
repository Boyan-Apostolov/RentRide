package nl.fontys.s3.rentride_be.business.impl.discount;

import nl.fontys.s3.rentride_be.domain.discount.DiscountPlan;
import nl.fontys.s3.rentride_be.domain.discount.DiscountPlanPurchase;
import nl.fontys.s3.rentride_be.domain.user.User;
import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanEntity;
import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanPurchaseEntity;
import nl.fontys.s3.rentride_be.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DiscountPlanPurchaseConverterTest {

    @Test
    void shouldConvertAllDiscountPlanPurchaseFieldsToDomain() {
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .name("Test User")
                .build();

        DiscountPlanEntity discountPlanEntity = DiscountPlanEntity.builder()
                .id(1L)
                .title("Test Plan")
                .description("Test Description")
                .remainingUses(10)
                .discountValue(20)
                .price(12)
                .build();

        DiscountPlanPurchaseEntity purchaseEntity = DiscountPlanPurchaseEntity.builder()
                .user(userEntity)
                .discountPlan(discountPlanEntity)
                .purchaseDate(LocalDateTime.of(2023, 10, 1, 12, 0))
                .remainingUses(5)
                .build();

        DiscountPlanPurchase actual = DiscountPlanPurchaseConverter.convert(purchaseEntity);

        DiscountPlan expectedDiscountPlan = DiscountPlan.builder()
                .id(1L)
                .title("Test Plan")
                .description("Test Description")
                .remainingUses(10)
                .discountValue(20)
                .price(12)
                .build();

        User expectedUser = User.builder()
                .id(1L)
                .name("Test User")
                .build();

        DiscountPlanPurchase expected = DiscountPlanPurchase.builder()
                .user(expectedUser)
                .discountPlan(expectedDiscountPlan)
                .purchaseDate(LocalDateTime.of(2023, 10, 1, 12, 0))
                .remainingUses(5)
                .build();

        assertEquals(expected, actual);
    }

    @Test
    void converterWithNullShouldReturnNull() {
        assertNull(DiscountPlanPurchaseConverter.convert(null));
    }
}