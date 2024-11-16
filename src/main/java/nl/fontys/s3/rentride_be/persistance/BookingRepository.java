package nl.fontys.s3.rentride_be.persistance;

import nl.fontys.s3.rentride_be.persistance.entity.BookingEntity;
import nl.fontys.s3.rentride_be.persistance.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    List<BookingEntity> findAllByCarId(Long carId);

    Long countByCarId(Long carId);

    //Hibernate does not support direct aggregate functions
    @Query("SELECT SUM(b.distance) FROM BookingEntity b WHERE b.car.id = :carId")
    Double sumDistanceByCarId(@Param("carId") Long carId);

    @Query("SELECT SUM(b.totalPrice) FROM BookingEntity b WHERE b.car.id = :carId")
    Double sumPricesByCarId(@Param("carId") Long carId);

    @Query("SELECT AVG((re.carCondition + re.carSpeed + re.valueForMoney) / 3) FROM BookingEntity b JOIN ReviewEntity re on b.id = re.booking.id WHERE b.car.id = :carId")
    Double avgRatingsByCarId(@Param("carId") Long carId);


    List<BookingEntity> findByUserId(Long userId);

    List<BookingEntity> findBookingsByCarIdOrderByStartDateTime(@Param("carId") Long carId);

    List<BookingEntity> findByEndDateTimeBeforeAndStatus(LocalDateTime endDateTime, BookingStatus status);

    List<BookingEntity> findByStartDateTimeBeforeAndStatus(LocalDateTime startDateTime, BookingStatus status);
}
