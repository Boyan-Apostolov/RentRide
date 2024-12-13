package nl.fontys.s3.rentride_be.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.city.*;
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

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok().body("Backend is working");
    }

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
    @RolesAllowed({"ADMIN"})

    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        deleteCityUseCase.deleteCity(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping()
    @RolesAllowed({"ADMIN"})

    public ResponseEntity<CreateCityResponse> createCity(@RequestBody @Valid CreateCityRequest request) {
        CreateCityResponse response = createCityUseCase.createCity(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("{id}")
    @RolesAllowed({"ADMIN"})

    public ResponseEntity<Void> updateCity(@PathVariable("id") long id,
                                           @RequestBody @Valid UpdateCityRequest request) {
        request.setId(id);
        updateCityUseCase.updateCity(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/lookupCity")
    @RolesAllowed({"ADMIN"})

    public ResponseEntity<Object> lookupCity(@RequestParam(value = "cityName") final String cityName) {
        GeoapifyResult possibleCity = lookupCityUseCase.lookupCity(cityName);
        if (possibleCity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("City not found by API");
        }
        return ResponseEntity.ok().body(possibleCity);
    }

    @GetMapping("/route")
    public ResponseEntity<GetRouteResponse> getRoute(@RequestParam(value = "fromCity") final Long fromCityId,
                                                     @RequestParam(value = "toCity") final Long toCityId){
        GetRouteResponse response = getRouteBetweenCitiesUseCase.getRoute(fromCityId, toCityId);
        return ResponseEntity.ok(response);
    }
}
