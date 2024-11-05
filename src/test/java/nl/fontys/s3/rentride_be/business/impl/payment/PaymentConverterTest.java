package nl.fontys.s3.rentride_be.business.impl.payment;

import nl.fontys.s3.rentride_be.domain.payment.Payment;
import nl.fontys.s3.rentride_be.persistance.entity.PaymentEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PaymentConverterTest {
    @Test
    void shouldConvertAllCountryFieldsToDomain(){
        PaymentEntity paymentToConvert = PaymentEntity
                .builder()
                .id(1L)
                .stripeLink("link")
                .user(null)
                .amount(12)
                .description("test")
                .isPaid(false)
                .build();

        Payment actual = PaymentConverter.convert(paymentToConvert);

        Payment expected = Payment
                .builder()
                .id(1L)
                .stripeLink("link")
                .user(null)
                .amount(12)
                .description("test")
                .isPaid(false)
                .build();

        assertEquals(expected, actual);
    }

    @Test
    void converterWithNullShouldReturnNull(){
        assertNull(PaymentConverter.convert(null));
    }
}
