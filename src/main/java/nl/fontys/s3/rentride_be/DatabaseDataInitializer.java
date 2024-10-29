package nl.fontys.s3.rentride_be;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.booking.UpdateBookingStatusUseCase;
import nl.fontys.s3.rentride_be.domain.car.CarFeatureType;
import nl.fontys.s3.rentride_be.persistance.*;
import nl.fontys.s3.rentride_be.persistance.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class DatabaseDataInitializer {
    private CityRepository cityRepository;
    private CarRepository carRepository;
    private UserRepository userRepository;
    private BookingRepository bookingRepository;
    private CarFeatureRepository carFeatureRepository;
    private UpdateBookingStatusUseCase updateBookingStatusUseCase;
    private static final Logger logger = LoggerFactory.getLogger(DatabaseDataInitializer.class);


    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initializeDatabase() {
        populateCities();
        populateCarFeatures();
        populateUsers();
        populateCars();

        tryFixMissedBookings();
    }

    private void tryFixMissedBookings() {
        LocalDateTime now = LocalDateTime.now();

        // Find paid bookings with passed start time that should be ACTIVE
        List<BookingEntity> paidBookings = bookingRepository.findPaidBookingsWithPassedStartTime(now);
        for (BookingEntity booking : paidBookings) {
            updateBookingStatusUseCase.updateBookingStatus(booking.getId(), BookingStatus.Active);
            logger.info("Paid booking activated: Booking ID {}", booking.getId());
        }

        // Find missed bookings (bookings that should be marked as FINISHED)
        List<BookingEntity> missedBookings = bookingRepository.findMissedBookings(now);
        for (BookingEntity booking : missedBookings) {
            updateBookingStatusUseCase.updateBookingStatus(booking.getId(), BookingStatus.Finished);
            logger.info("Missed booking fixed: Booking ID {}", booking.getId());
        }
    }

    private void populateCarFeatures() {
        if (carFeatureRepository.count() == 0) {
            this.carFeatureRepository.save(CarFeatureEntity.builder()
                    .featureType(CarFeatureType.Seats)
                    .featureText("5")
                    .build());

            this.carFeatureRepository.save(CarFeatureEntity.builder()
                    .featureType(CarFeatureType.Doors)
                    .featureText("4")
                    .build());

            this.carFeatureRepository.save(CarFeatureEntity.builder()
                    .featureType(CarFeatureType.Transmission)
                    .featureText("Automatic")
                    .build());

            this.carFeatureRepository.save(CarFeatureEntity.builder()
                    .featureType(CarFeatureType.Transmission)
                    .featureText("Manual")
                    .build());

            this.carFeatureRepository.save(CarFeatureEntity.builder()
                    .featureType(CarFeatureType.Bonus)
                    .featureText("AC")
                    .build());
        }
    }

    private void populateCities() {
        if (this.cityRepository.count() == 0) {
            String street = "Some street";
            this.cityRepository.save(CityEntity.builder().name("Eindhoven").lat(51.4231).lon(5.4623).depoAdress(street).build());
            this.cityRepository.save(CityEntity.builder().name("Amsterdam").lat(52.3676).lon(4.9041).depoAdress(street).build());
            this.cityRepository.save(CityEntity.builder().name("Breda").lat(51.5719).lon(4.7683).depoAdress(street).build());
            this.cityRepository.save(CityEntity.builder().name("Utrecht").lat(52.0907).lon(5.1214).depoAdress(street).build());
        }
    }

    private void populateCars() {
        String featureNotFoundText = "Feature not found";
        if (this.carRepository.count() == 0) {
            CarEntity car1 = CarEntity.builder()
                    .make("Ford")
                    .model("Fiesta")
                    .registrationNumber("BT2142KX")
                    .fuelConsumption(6.5)
                    .city(this.cityRepository.findById(1L).orElse(null)) // Assuming Eindhoven
                    .photosBase64(List.of("https://i.ibb.co/WP7L59F/IMG-20220401-152405.jpg"))
                    .build();

            List<CarFeatureEntity> features1 = List.of(
                    carFeatureRepository.findById(1L).orElseThrow(() -> new RuntimeException(featureNotFoundText)),
                    carFeatureRepository.findById(2L).orElseThrow(() -> new RuntimeException(featureNotFoundText)),
                    carFeatureRepository.findById(3L).orElseThrow(() -> new RuntimeException(featureNotFoundText)),
                    carFeatureRepository.findById(5L).orElseThrow(() -> new RuntimeException(featureNotFoundText))
            );
            car1.setFeatures(features1);

            CarEntity car2 = CarEntity.builder()
                    .make("Audi")
                    .model("RS5")
                    .registrationNumber("AB1234CD")
                    .fuelConsumption(12.4)
                    .city(this.cityRepository.findById(2L).orElse(null)) // Assuming Amsterdam
                    .photosBase64(List.of("https://i.ibb.co/5ccy8Qt/H4s-IAAAAAAAAAFvzlo-G1t-Ii-BOTrayfuvp-Gh6-m1z-Jga-Gig-IGBg-ZGo-Dh-Tt-NOaz-I-2-Dh-CHs-CEtz-Ew-F-Sl-Mw.png"))
                    .build();

            List<CarFeatureEntity> features2 = List.of(
                    carFeatureRepository.findById(1L).orElseThrow(() -> new RuntimeException(featureNotFoundText)), // Seats
                    carFeatureRepository.findById(2L).orElseThrow(() -> new RuntimeException(featureNotFoundText)), // Doors
                    carFeatureRepository.findById(4L).orElseThrow(() -> new RuntimeException(featureNotFoundText)), // Transmission - Manual
                    carFeatureRepository.findById(5L).orElseThrow(() -> new RuntimeException(featureNotFoundText))  // AC
            );
            car2.setFeatures(features2);

            CarEntity car4 = CarEntity.builder()
                    .make("BMW")
                    .model("M5")
                    .registrationNumber("GH9012IJ")
                    .fuelConsumption(17.0)
                    .city(this.cityRepository.findById(4L).orElse(null)) // Assuming Utrecht
                    .photosBase64(List.of("https://i.ibb.co/fr5q7JP/bmw-m-series-seo-overview-ms-04.jpg"))
                    .build();

            List<CarFeatureEntity> features4 = List.of(
                    carFeatureRepository.findById(1L).orElseThrow(() -> new RuntimeException(featureNotFoundText)),
                    carFeatureRepository.findById(2L).orElseThrow(() -> new RuntimeException(featureNotFoundText)),
                    carFeatureRepository.findById(3L).orElseThrow(() -> new RuntimeException(featureNotFoundText)),
                    carFeatureRepository.findById(4L).orElseThrow(() -> new RuntimeException(featureNotFoundText)),
                    carFeatureRepository.findById(5L).orElseThrow(() -> new RuntimeException(featureNotFoundText))
            );
            car4.setFeatures(features4);

            this.carRepository.saveAll(List.of(car1, car2, car4));
        }
    }

    private void populateUsers() {
        if (this.userRepository.count() == 0) {
            this.userRepository.save(UserEntity
                    .builder()
                    .name("Boyan Apostolov")
                    .role(UserRole.Admin)
                    .email("admin@admin.com")
                    .password("12345678")
                    .birthDate(LocalDate.of(2004, 3, 12))
                    .build());
        }
    }
}
