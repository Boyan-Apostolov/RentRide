package nl.fontys.s3.rentride_be.persistance;

import nl.fontys.s3.rentride_be.persistance.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    List<BookingEntity> findByCarId(Long carId);

    List<BookingEntity> findByUserId(Long userId);

    List<BookingEntity> findBookingsByCarIdOrderByStartDateTime(@Param("carId") Long carId);

    @Query("SELECT b FROM BookingEntity b WHERE b.endDateTime < :currentTime AND b.status = 2")
    List<BookingEntity> findMissedBookings(@Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT b FROM BookingEntity b WHERE b.startDateTime < :currentTime AND b.status = 1")
    List<BookingEntity> findPaidBookingsWithPassedStartTime(@Param("currentTime") LocalDateTime currentTime);
}
