package nl.fontys.s3.rentride_be.business.impl.car;

import nl.fontys.s3.rentride_be.domain.car.Car;
import nl.fontys.s3.rentride_be.domain.city.City;
import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CarConverterTest {
    @Test
    void shouldConvertAllCountryFieldsToDomain(){
        CarEntity carToBeConverted = CarEntity
                .builder()
                .id(1L)
                .make("test")
                .model("test")
                .registrationNumber("test")
                .fuelConsumption(1.2)
                .city(CityEntity.builder().build())
                .photosBase64(List.of(""))
                .features(List.of())
                .build();

        Car actual = CarConverter.convert(carToBeConverted);

        Car expected = Car
                .builder()
                .id(1L)
                .make("test")
                .model("test")
                .registrationNumber("test")
                .fuelConsumption(1.2)
                .city(City.builder().build())
                .photosBase64(List.of(""))
                .carFeatures(List.of())
                .build();


        assertEquals(expected, actual);
    }
}
