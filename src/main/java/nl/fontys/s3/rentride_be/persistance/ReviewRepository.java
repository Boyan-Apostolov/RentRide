package nl.fontys.s3.rentride_be.persistance;

import nl.fontys.s3.rentride_be.persistance.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    List<ReviewEntity> findAllByBooking_CarId(Long carId);

    @Query(value = "CALL avg_ratings_by_car_id(:carId);", nativeQuery = true)
    Double getAverageRatingsByCar(@Param("carId") Long carId);
}
