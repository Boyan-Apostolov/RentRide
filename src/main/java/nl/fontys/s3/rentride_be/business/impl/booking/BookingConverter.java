package nl.fontys.s3.rentride_be.business.impl.booking;

import nl.fontys.s3.rentride_be.business.impl.car.CarConverter;
import nl.fontys.s3.rentride_be.business.impl.user.UserConverter;
import nl.fontys.s3.rentride_be.domain.booking.Booking;
import nl.fontys.s3.rentride_be.persistance.entity.BookingEntity;
import nl.fontys.s3.rentride_be.business.impl.city.CityConverter;

public final class BookingConverter {
    private BookingConverter() {}

    public static Booking convert(BookingEntity booking) {
        if(booking == null) return null;

        return Booking.builder()
                .id(booking.getId())
                .bookingStatus(booking.getBookingStatus())
                .startCity(CityConverter.convert(booking.getStartCity()))
                .endCity(CityConverter.convert(booking.getEndCity()))
                .startDateTime(booking.getStartDateTime())
                .endDateTime(booking.getEndDateTime())
                .user(UserConverter.convert(booking.getUser()))
                .car(CarConverter.convert(booking.getCar()))
                .distance(booking.getDistance())
                .totalPrice(booking.getTotalPrice())
                .build();
    }
}
