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

    List<BookingEntity> findByUserId(Long userId);

    List<BookingEntity> findBookingsByCarIdOrderByStartDateTime(@Param("carId") Long carId);

    List<BookingEntity> findByEndDateTimeBeforeAndStatus(LocalDateTime endDateTime, BookingStatus status);

    List<BookingEntity> findByStartDateTimeBeforeAndStatus(LocalDateTime startDateTime, BookingStatus status);

    @Query(value = "CALL sum_distance_by_car(:carId)", nativeQuery = true)
    Double getTotalDistanceByCar(@Param("carId") Long carId);

    @Query(value = "CALL sum_all_distances()", nativeQuery = true)
    Double getTotalDistance();

    @Query(value = "CALL sum_price_by_car(:carId);", nativeQuery = true)
    Double getTotalPriceByCar(@Param("carId") Long carId);

    @Query(value = "CALL sum_all_prices()", nativeQuery = true)
    Double getTotalPrice();

    @Query(value = "CALL get_most_popular_cars()", nativeQuery = true)
    List<Object[]> getMostPopularCars();

    @Query(value = "CALL get_most_popular_trips()", nativeQuery = true)
    List<Object[]> getMostPopularTrips();

    @Query(value = "CALL get_bookings_per_month()", nativeQuery = true)
    List<Object[]> getBookingsPerMonth();

    @Query(value = "CALL get_popular_cars_over_time()", nativeQuery = true)
    List<Object[]> getPopularCarsOverTime();
}
