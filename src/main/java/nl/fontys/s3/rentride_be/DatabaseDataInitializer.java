package nl.fontys.s3.rentride_be;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.domain.car.CarFeatureType;
import nl.fontys.s3.rentride_be.persistance.BookingRepository;
import nl.fontys.s3.rentride_be.persistance.CarRepository;
import nl.fontys.s3.rentride_be.persistance.CityRepository;
import nl.fontys.s3.rentride_be.persistance.UserRepository;
import nl.fontys.s3.rentride_be.persistance.entity.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@AllArgsConstructor
public class DatabaseDataInitializer {
    private CityRepository cityRepository;
    private CarRepository carRepository;
    private UserRepository userRepository;
    private BookingRepository bookingRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeDatabase() {
        populateCities();
        populateCars();
        populateUsers();
    }

    private void populateCities(){
        if(this.cityRepository.count() == 0){
            this.cityRepository.save(CityEntity.builder().name("Eindhoven").lat(51.4231).lon(5.4623).depoAdress("Some street 1").build());
            this.cityRepository.save(CityEntity.builder().name("Amsterdam").lat(52.3676).lon(4.9041).depoAdress("Some street 1").build());
            this.cityRepository.save(CityEntity.builder().name("Breda").lat(51.5719).lon(4.7683).depoAdress("Some street 1").build());
            this.cityRepository.save(CityEntity.builder().name("Utrecht").lat(52.0907).lon(5.1214).depoAdress("Some street 1").build());
        }
    }

    private void populateCars(){
        if(this.carRepository.count() == 0){
            this.carRepository.save(
                    CarEntity.builder()
                            .make("Ford")
                            .model("Fiesta")
                            .registrationNumber("BT2142KX")
                            .fuelConsumption(6.1)
                            .city(this.cityRepository.findById(1))
                            .features(List.of(
                                    CarFeatureEntity.builder()
                                            .id(1L)
                                            .featureType(CarFeatureType.Seats)
                                            .featureText("5")
                                            .build(),
                                    CarFeatureEntity.builder()
                                            .id(2L)
                                            .featureType(CarFeatureType.Doors)
                                            .featureText("4")
                                            .build(),
                                    CarFeatureEntity.builder()
                                            .id(3L)
                                            .featureType(CarFeatureType.Transmission)
                                            .featureText("Manual")
                                            .build(),
                                    CarFeatureEntity.builder()
                                            .id(4L)
                                            .featureType(CarFeatureType.Bonus)
                                            .featureText("A/C")
                                            .build()
                            ))
                            .photosBase64(List.of("https://i.ibb.co/fXZvs3p/3592-BEF4-7226-4-B22-ACBE-FE58-D182-A90-D-1-105-c.jpg"))
                            .build()
            );
        }
    }

    private void populateUsers() {
        if(this.userRepository.count() == 0){
            this.userRepository.save(UserEntity
                    .builder()
                            .Name("Boyan Apostolov")
                            .Role(UserRole.Admin)
                            .Email("admin@admin.com")
                            .Password("12345678")
                            .CustomerId("x_213")
                            .BirthDate(new Date(2004,3,12))
                    .build());
            this.userRepository.save(UserEntity
                    .builder()
                    .Name("Some Customer")
                    .Role(UserRole.Customer)
                    .Email("customer@customer.com")
                    .Password("12345678")
                    .CustomerId("x_2134567")
                    .BirthDate(new Date(2005,7,2))
                    .build());
        }
    }
}
