package nl.fontys.s3.rentride_be.business.impl.car;

import nl.fontys.s3.rentride_be.domain.car.CarFeature;
import nl.fontys.s3.rentride_be.domain.car.CarFeatureType;
import nl.fontys.s3.rentride_be.persistance.entity.CarFeatureEntity;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;

class CarFeatureConverterTest {
    @Test
    void shouldConvertAllCountryFieldsToDomain(){
        CarFeatureEntity carFeatureToBeConverted = CarFeatureEntity
                .builder()
                .id(1L)
                .featureType(CarFeatureType.Seats)
                .featureText("test")
                .build();

        CarFeature actual = CarFeatureConverter.convert(carFeatureToBeConverted);

        CarFeature expected = CarFeature
                .builder()
                .id(1L)
                .featureType(CarFeatureType.Seats)
                .featureText("test")
                .build();

        assertEquals(expected, actual);
    }
}
