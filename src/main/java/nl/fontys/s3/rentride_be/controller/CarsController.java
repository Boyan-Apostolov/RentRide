package nl.fontys.s3.rentride_be.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.useCases.car.*;
import nl.fontys.s3.rentride_be.business.useCases.city.GetCityUseCase;
import nl.fontys.s3.rentride_be.domain.car.*;
import nl.fontys.s3.rentride_be.domain.city.City;
import nl.fontys.s3.rentride_be.domain.city.CreateCityRequest;
import nl.fontys.s3.rentride_be.domain.city.CreateCityResponse;
import nl.fontys.s3.rentride_be.domain.city.UpdateCityRequest;
import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cars")
@AllArgsConstructor
public class CarsController {
    private GetCarUseCase getCarUseCase;
    private GetCarsUseCase getCarsUseCase;
    private DeleteCarUseCase deleteCarUseCase;
    private CreateCarUseCase createCarUseCase;
    private UpdateCarUseCase updateCarUseCase;
    private GetAvailableCarsUseCase getAvailableCarsUseCase;
    private GetAllCarFeatures getAllCarFeatures;

    @GetMapping("{id}")
    public ResponseEntity<Car> getCar(@PathVariable(value = "id") final long id) {
        Car carOptional = getCarUseCase.getCar(id);

        if (carOptional == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(carOptional);
    }

    @GetMapping
    public ResponseEntity<List<Car>> getAllCars() {
        return ResponseEntity.ok(this.getCarsUseCase.getCars());
    }

    @PostMapping("availableCars")
    public ResponseEntity<List<Car>> getAvailableCars(@RequestBody @Valid GetAvailableCarsRequest request) {
       //TODO: implement bookings and filter availability
        List<Car> foundCars = getAvailableCarsUseCase.getAvailableCars(request);
        return ResponseEntity.ok(foundCars);
    }

    @GetMapping("features")
    public ResponseEntity<List<CarFeature>> getAllFeatures() {
        List<CarFeature> carFeatures = getAllCarFeatures.getAllCarFeatures();
        return ResponseEntity.ok(carFeatures);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        deleteCarUseCase.deleteCar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping()
    public ResponseEntity<CreateCarResponse> createCar(@RequestBody @Valid CreateCarRequest request) {
        CreateCarResponse response = createCarUseCase.createCar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateCar(@PathVariable("id") long id,
                                           @RequestBody @Valid UpdateCarRequest request) {
        request.setId(id);
        updateCarUseCase.updateCar(request);
        return ResponseEntity.noContent().build();
    }
}
