package nl.fontys.s3.rentride_be.persistance;

import nl.fontys.s3.rentride_be.persistance.entity.BookingEntity;
import nl.fontys.s3.rentride_be.persistance.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    List<BookingEntity> findAllByCarId(Long carId);

    Long countByCarId(Long carId);

    List<BookingEntity> findByUserId(Long userId);

    List<BookingEntity> findBookingsByCarIdOrderByStartDateTime(@Param("carId") Long carId);

    List<BookingEntity> findByEndDateTimeBeforeAndStatus(LocalDateTime endDateTime, BookingStatus status);

    List<BookingEntity> findByStartDateTimeBeforeAndStatus(LocalDateTime startDateTime, BookingStatus status);
}
