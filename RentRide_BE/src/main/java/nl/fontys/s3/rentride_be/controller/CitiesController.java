package nl.fontys.s3.rentride_be.controller;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.domain.City;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cities")
@AllArgsConstructor
public class CitiesController {
    private CityService cityService;

    @GetMapping
    public List<City> getCities() {
        return this.cityService.getCities();
    }

    @PostMapping
    public void addCity(@RequestBody City city) {
        this.cityService.addCity(city);
    }
}
