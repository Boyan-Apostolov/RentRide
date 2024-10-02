package nl.fontys.s3.rentride_be.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.useCases.city.*;
import nl.fontys.s3.rentride_be.domain.city.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cities")
@AllArgsConstructor
public class CitiesController {
    private GetCitiesUseCase getCitiesUseCase;
    private GetCityUseCase getCityUseCase;
    private DeleteCityUseCase deleteCityUseCase;
    private CreateCityUseCase createCityUseCase;
    private UpdateCityUseCase updateCityUseCase;
    private LookupCityUseCase lookupCityUseCase;
    private GetRouteBetweenCitiesUseCase getRouteBetweenCitiesUseCase;

    @GetMapping("{id}")
    public ResponseEntity<City> getCity(@PathVariable(value = "id") final long id) {
        City cityOptional = getCityUseCase.getCity(id);
        if (cityOptional == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(cityOptional);
    }

    @GetMapping
    public ResponseEntity<List<City>> getAllCities() {
        return ResponseEntity.ok(this.getCitiesUseCase.getCities());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        deleteCityUseCase.deleteCity(id);
        return ResponseEntity.noContent().build();

        //TODO: Validate if there are cars in the city and move them to a GeneralCity
    }

    @PostMapping()
    public ResponseEntity<CreateCityResponse> createCity(@RequestBody @Valid CreateCityRequest request) {
        CreateCityResponse response = createCityUseCase.createCity(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateCity(@PathVariable("id") long id,
                                           @RequestBody @Valid UpdateCityRequest request) {
        request.setId(id);
        updateCityUseCase.updateCity(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/lookupCity")
    public ResponseEntity<GeoapifyResult> lookupCity(@RequestParam(value = "cityName") final String cityName) {
        GeoapifyResult possibleCity = lookupCityUseCase.lookupCity(cityName);
        if (possibleCity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(possibleCity);
    }

    @GetMapping("/route")
    public ResponseEntity<GetRouteResponse> getRoute(@RequestParam(value = "fromCity") final String fromCity,
                                                     @RequestParam(value = "toCity") final String toCity){
        GetRouteResponse response = getRouteBetweenCitiesUseCase.getRoute(fromCity, toCity);
        return ResponseEntity.ok(response);
    }
}
