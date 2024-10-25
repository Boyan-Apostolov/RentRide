package nl.fontys.s3.rentride_be;

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
    public void initializeDatabase() {
        populateCities();
        populateCarFeatures();
        populateUsers();

        tryFixMissedBookings();
    }

    private void tryFixMissedBookings(){
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
        if(carFeatureRepository.count() == 0){
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

    private void populateCities(){
        if(this.cityRepository.count() == 0){
            String street = "Some street";
            this.cityRepository.save(CityEntity.builder().name("Eindhoven").lat(51.4231).lon(5.4623).depoAdress(street).build());
            this.cityRepository.save(CityEntity.builder().name("Amsterdam").lat(52.3676).lon(4.9041).depoAdress(street).build());
            this.cityRepository.save(CityEntity.builder().name("Breda").lat(51.5719).lon(4.7683).depoAdress(street).build());
            this.cityRepository.save(CityEntity.builder().name("Utrecht").lat(52.0907).lon(5.1214).depoAdress(street).build());
        }
    }

    private void populateUsers() {
        if(this.userRepository.count() == 0){
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
