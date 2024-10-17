package nl.fontys.s3.rentride_be;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.domain.car.CarFeatureType;
import nl.fontys.s3.rentride_be.persistance.*;
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
    private CarFeatureRepository carFeatureRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeDatabase() {
        populateCities();
        populateCarFeatures();
        //populateCars();
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
                            .city(this.cityRepository.findById(1L).get())
                            .features(List.of(
                                    this.carFeatureRepository.findById(1L).get(),
                                    this.carFeatureRepository.findById(2L).get(),
                                    this.carFeatureRepository.findById(3L).get(),
                                    this.carFeatureRepository.findById(5L).get()
                            ))
                            .photosBase64(List.of("https://i.ibb.co/fXZvs3p/3592-BEF4-7226-4-B22-ACBE-FE58-D182-A90-D-1-105-c.jpg"))
                            .build()
            );

            this.carRepository.save(
                    CarEntity.builder()
                            .make("Ford")
                            .model("Fiesta 2")
                            .registrationNumber("nederland")
                            .fuelConsumption(9.1)
                            .city(this.cityRepository.findById(2L).get())
                                    .features(List.of(
                                            this.carFeatureRepository.findById(1L).get(),
                                            this.carFeatureRepository.findById(2L).get(),
                                            this.carFeatureRepository.findById(4L).get(),
                                            this.carFeatureRepository.findById(5L).get()
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
