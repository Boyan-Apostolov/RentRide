package nl.fontys.s3.rentride_be.business.impl.discount;

import nl.fontys.s3.rentride_be.domain.discount.DiscountPlan;
import nl.fontys.s3.rentride_be.persistance.entity.DiscountPlanEntity;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DiscountPlanConverterTest {

    @Test
    void shouldConvertAllDiscountPlanFieldsToDomain() {
        DiscountPlanEntity discountPlanToBeConverted = DiscountPlanEntity.builder()
                .id(1L)
                .title("Test Plan")
                .description("Test Description")
                .remainingUses(5)
                .discountValue(15)
                .price(99.99)
                .build();

        DiscountPlan actual = DiscountPlanConverter.convert(discountPlanToBeConverted);

        DiscountPlan expected = DiscountPlan.builder()
                .id(1L)
                .title("Test Plan")
                .description("Test Description")
                .remainingUses(5)
                .discountValue(15)
                .price(99.99)
                .build();

        assertEquals(expected, actual);
    }

    @Test
    void converterWithNullShouldReturnNull() {
        assertNull(DiscountPlanConverter.convert(null));
    }
}