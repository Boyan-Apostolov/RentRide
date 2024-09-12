package nl.fontys.s3.rentride_be.controller;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.useCases.city.GetCitiesUseCase;
import nl.fontys.s3.rentride_be.domain.city.City;
import nl.fontys.s3.rentride_be.domain.city.GetAllCitiesResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cities")
@AllArgsConstructor
public class CitiesController {
    private GetCitiesUseCase getCitiesUseCase;

    @GetMapping
    public ResponseEntity<GetAllCitiesResponse> getAllCities() {
        return ResponseEntity.ok(this.getCitiesUseCase.getCities());
    }
}
