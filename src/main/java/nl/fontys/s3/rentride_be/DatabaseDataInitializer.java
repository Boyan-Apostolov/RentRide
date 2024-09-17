package nl.fontys.s3.rentride_be;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.domain.car.CarTransmissionType;
import nl.fontys.s3.rentride_be.persistance.CarRepository;
import nl.fontys.s3.rentride_be.persistance.CityRepository;
import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DatabaseDataInitializer {
    private CityRepository cityRepository;
    private CarRepository carRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeDatabase() {
        populateCities();
        populateCars();
    }

    private void populateCities(){
        if(this.cityRepository.count() == 0){
            this.cityRepository.save(CityEntity.builder().name("Eindhoven").lat(51.4231).lon(5.4623).build());
            this.cityRepository.save(CityEntity.builder().name("Amsterdam").lat(52.3676).lon(4.9041).build());
            this.cityRepository.save(CityEntity.builder().name("Breda").lat(51.5719).lon(4.7683).build());
            this.cityRepository.save(CityEntity.builder().name("Utrecht").lat(52.0907).lon(5.1214).build());
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
                            .seatsCount(5)
                            .transmissionType(CarTransmissionType.Manual)
                            .city(this.cityRepository.findById(1))
                            .build()
            );

            this.carRepository.save(
                    CarEntity.builder()
                            .make("Volksawagen")
                            .model("Tuaran")
                            .registrationNumber("BT7287KR")
                            .fuelConsumption(5.5)
                            .seatsCount(5)
                            .transmissionType(CarTransmissionType.Manual)
                            .city(this.cityRepository.findById(2))
                            .build()
            );

            this.carRepository.save(
                    CarEntity.builder()
                            .make("BMW")
                            .model("M4")
                            .registrationNumber("XAJ-2141-XZK")
                            .fuelConsumption(10.5)
                            .seatsCount(5)
                            .transmissionType(CarTransmissionType.Automatic)
                            .city(this.cityRepository.findById(3))
                            .build()
            );
        }
    }
}
