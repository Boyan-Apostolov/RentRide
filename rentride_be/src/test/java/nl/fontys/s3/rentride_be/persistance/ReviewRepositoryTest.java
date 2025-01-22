package nl.fontys.s3.rentride_be.persistance;

import jakarta.persistence.EntityManager;
import nl.fontys.s3.rentride_be.persistance.entity.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ReviewRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CityRepository  cityRepository;
    @Autowired
    private CarRepository carRepository;

    private CityEntity saveCity(){
        CityEntity initialCity = CityEntity.builder()
                .name("Eindhoven")
                .lat(10.0)
                .lon(10.0)
                .depoAdress("Test str")
                .build();
        return cityRepository.save(initialCity);
    }

    private UserEntity saveUser() {
        UserEntity user = UserEntity.builder()
                .email("user@example.com")
                .name("Test User")
                .password("password")
                .birthDate(LocalDate.now())
                .build();
        return userRepository.save(user);
    }

    private CarEntity saveCar(){
        CarEntity carEntity = CarEntity.builder()
                .make("a")
                .fuelConsumption(1.1)
                .model("2")
                .city(saveCity())
                .features(List.of())
                .model("")
                .registrationNumber("")
                .build();
        return carRepository.save(carEntity);
    }

    private BookingEntity saveBooking(UserEntity user) {
        BookingEntity booking = BookingEntity.builder()
                .startDateTime(LocalDateTime.now().minusDays(5))
                .endDateTime(LocalDateTime.now())
                .user(user)
                .status(BookingStatus.Paid)
                .coverage(BookingCoverage.Premium)
                .startCity(saveCity())
                .endCity(saveCity())
                .car(saveCar())
                .distance(100.0)
                .totalPrice(500.0)
                .paymentId("!")
                .build();
        return bookingRepository.save(booking);
    }

    @Test
    void save_ShouldSaveReviewWithAllFields() {
        UserEntity user = saveUser();
        BookingEntity booking = saveBooking(user);

        ReviewEntity review = ReviewEntity.builder()
                .createdOn(LocalDateTime.now())
                .text("Great car and service!")
                .user(user)
                .booking(booking)
                .valueForMoney(5)
                .carCondition(5)
                .carSpeed(4)
                .build();

        ReviewEntity savedReview = reviewRepository.save(review);
        assertNotNull(savedReview.getId());

        savedReview = entityManager.find(ReviewEntity.class, savedReview.getId());
        review.setId(savedReview.getId());

        assertEquals(review, savedReview);
    }

    @Test
    void findAllByBooking_CarId_ShouldReturnReviewsForCar() {
        UserEntity user = saveUser();
        BookingEntity booking = saveBooking(user);

        ReviewEntity review = ReviewEntity.builder()
                .createdOn(LocalDateTime.now())
                .text("Excellent experience!")
                .user(user)
                .booking(booking)
                .valueForMoney(4)
                .carCondition(5)
                .carSpeed(3)
                .build();
        reviewRepository.save(review);

        List<ReviewEntity> reviews = reviewRepository.findAllByBooking_CarId(booking.getCar().getId());
        assertFalse(reviews.isEmpty());
        assertEquals(review.getText(), reviews.get(0).getText());
    }

    @Test
    void findAllByUser_Id_ShouldReturnReviewsForUser() {
        UserEntity user = saveUser();
        BookingEntity booking = saveBooking(user);

        ReviewEntity review = ReviewEntity.builder()
                .createdOn(LocalDateTime.now())
                .text("Would book again!")
                .user(user)
                .booking(booking)
                .valueForMoney(5)
                .carCondition(5)
                .carSpeed(4)
                .build();
        reviewRepository.save(review);

        List<ReviewEntity> reviews = reviewRepository.findAllByUser_Id(user.getId());
        assertFalse(reviews.isEmpty());
        assertEquals(review.getText(), reviews.get(0).getText());
    }
}