package nl.fontys.s3.rentride_be.persistance.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingEntity {
    private Long id;
    private BookingStatus bookingStatus;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private CityEntity startCity;
    private CityEntity endCity;
    private CarEntity car;
    private UserEntity user;
    private Long distance;
    private Long totalPrice;
}
