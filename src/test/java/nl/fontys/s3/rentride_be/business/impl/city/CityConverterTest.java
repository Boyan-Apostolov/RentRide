package nl.fontys.s3.rentride_be.business.impl.city;

import nl.fontys.s3.rentride_be.domain.city.City;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CityConverterTest {

    @Test
    void shouldConvertAllCountryFieldsToDomain(){
        CityEntity cityToBeConverted = CityEntity
                .builder()
                .id(1L)
                .name("test")
                .lat(10.0)
                .lon(10.0)
                .depoAdress("test addr")
                .build();

        City actual = CityConverter.convert(cityToBeConverted);

        City expected = City
                .builder()
                .id(1L)
                .name("test")
                .lat(10.0)
                .lon(10.0)
                .depoAddress("test addr")
                .build();


        assertEquals(expected, actual);
    }

    @Test
    void converterWithNullShouldReturnNull(){
        assertNull(CityConverter.convert(null));
    }
}
