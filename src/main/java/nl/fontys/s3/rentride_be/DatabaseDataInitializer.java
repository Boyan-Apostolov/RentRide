package nl.fontys.s3.rentride_be;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.domain.car.CarFeatureType;
import nl.fontys.s3.rentride_be.persistance.*;
import nl.fontys.s3.rentride_be.persistance.entity.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class DatabaseDataInitializer {
    private CityRepository cityRepository;
    private CarRepository carRepository;
    private UserRepository userRepository;
    private BookingRepository bookingRepository;
    private CarFeatureRepository carFeatureRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeDatabase() {
        populateCities();
        populateCarFeatures();
        populateUsers();
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
