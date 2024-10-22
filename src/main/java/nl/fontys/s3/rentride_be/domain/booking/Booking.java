package nl.fontys.s3.rentride_be.domain.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import nl.fontys.s3.rentride_be.domain.car.Car;
import nl.fontys.s3.rentride_be.domain.city.City;
import nl.fontys.s3.rentride_be.domain.user.User;
import nl.fontys.s3.rentride_be.persistance.entity.BookingStatus;
import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
import nl.fontys.s3.rentride_be.persistance.entity.UserEntity;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class Booking {
    private Long id;
    private BookingStatus bookingStatus;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private City startCity;
    private City endCity;
    private Car car;
    private User user;
    private double distance;
    private double totalPrice;
}
