package nl.fontys.s3.rentride_be.persistance;

import nl.fontys.s3.rentride_be.persistance.entity.BookingEntity;
import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository {
    List<BookingEntity> findAll();

    List<BookingEntity> findByCarId(Long carId);

    List<BookingEntity> findByUserId(Long userId);

    List<BookingEntity> findByCarIdAndDates(Long carId, LocalDateTime startDate, LocalDateTime endDate);

    BookingEntity findById(long bookingId);

    BookingEntity save(BookingEntity car);

    int count();
}
