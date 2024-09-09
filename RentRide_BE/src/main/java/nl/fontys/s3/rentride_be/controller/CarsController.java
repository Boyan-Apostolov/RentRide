package nl.fontys.s3.rentride_be.controller;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.CarService;
import nl.fontys.s3.rentride_be.domain.Car;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cars")
@AllArgsConstructor
public class CarsController {
    private CarService carService;

    @GetMapping
    public List<Car> getCars() {
        return this.carService.getCars();
    }

    @PostMapping
    public void addCar(@RequestBody Car car) {
        this.carService.addCar(car);
    }
}
