package nl.fontys.s3.rentride_be.business.impl.booking;

import nl.fontys.s3.rentride_be.domain.booking.Booking;
import nl.fontys.s3.rentride_be.domain.car.Car;
import nl.fontys.s3.rentride_be.domain.city.City;
import nl.fontys.s3.rentride_be.domain.user.User;
import nl.fontys.s3.rentride_be.persistance.entity.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BookingConverterTest {
    @Test
    void shouldConvertAllCountryFieldsToDomain(){
        BookingEntity bookingToBeConverted = BookingEntity
                .builder()
                .id(1L)
                .status(BookingStatus.Active)
                .startCity(CityEntity.builder().build())
                .endCity(CityEntity.builder().build())
                .startDateTime(LocalDateTime.of(2000, 12,12, 2, 2))
                .endDateTime(LocalDateTime.of(2000, 12,12, 2, 2))
                .user(UserEntity.builder().build())
                .car(CarEntity.builder().features(List.of()).build())
                .distance(12.12)
                .totalPrice(12.12)
                .build();

        Booking actual = BookingConverter.convert(bookingToBeConverted);

        Booking expected = Booking.builder()
                .id(1L)
                .bookingStatus(BookingStatus.Active)
                .startCity(City.builder().build())
                .endCity(City.builder().build())
                .startDateTime(LocalDateTime.of(2000, 12,12, 2, 2))
                .endDateTime(LocalDateTime.of(2000, 12,12, 2, 2))
                .user(User.builder().build())
                .car(Car.builder().carFeatures(List.of()).build())
                .distance(12.12)
                .totalPrice(12.12)
                .build();


        assertEquals(expected, actual);
    }

    @Test
    void converterWithNullShouldReturnNull(){
        assertNull(BookingConverter.convert(null));
    }
}
