package nl.fontys.s3.rentride_be.business.impl.damage;

import nl.fontys.s3.rentride_be.domain.damage.Damage;
import nl.fontys.s3.rentride_be.persistance.entity.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DamageConverterTest {
    @Test
    void shouldConvertAllCountryFieldsToDomain(){
        DamageEntity damageToBeConverted = DamageEntity
                .builder()
                .id(1L)
                .name("test")
                .iconUrl("test")
                .cost(12)
                .build();

        Damage actual = DamageConverter.convert(damageToBeConverted);

        Damage expected = Damage.builder()
                .id(1L)
                .id(1L)
                .name("test")
                .iconUrl("test")
                .cost(12)
                .build();


        assertEquals(expected, actual);
    }

     @Test
     void converterWithNullShouldReturnNull(){
         assertNull(DamageConverter.convert(null));
     }
}
