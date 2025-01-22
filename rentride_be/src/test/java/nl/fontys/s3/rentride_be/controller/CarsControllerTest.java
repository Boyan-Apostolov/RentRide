package nl.fontys.s3.rentride_be.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import nl.fontys.s3.rentride_be.business.use_cases.car.*;
import nl.fontys.s3.rentride_be.domain.car.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "10@fontys.nl", roles = {"ADMIN"})
class CarsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetCarUseCase getCarUseCase;

    @MockBean
    private GetCarsUseCase getCarsUseCase;

    @MockBean
    private DeleteCarUseCase deleteCarUseCase;

    @MockBean
    private CreateCarUseCase createCarUseCase;

    @MockBean
    private UpdateCarUseCase updateCarUseCase;

    @MockBean
    private GetAvailableCarsUseCase getAvailableCarsUseCase;

    @MockBean
    private GetAllCarFeatures getAllCarFeatures;

    @MockBean
    private GetExclusiveCarsUseCase getExclusiveCarsUseCase;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getCar_shouldReturn200WithCar_WhenCarFound() throws Exception {
        Car car = Car.builder().id(1L).make("Toyota").model("Corolla").build();
        when(getCarUseCase.getCar(1L)).thenReturn(car);

        mockMvc.perform(get("/cars/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(car)));

        verify(getCarUseCase).getCar(1L);
    }

    @Test
    void getCar_shouldReturn404_WhenCarNotFound() throws Exception {
        when(getCarUseCase.getCar(1L)).thenReturn(null);

        mockMvc.perform(get("/cars/1"))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(getCarUseCase).getCar(1L);
    }

    @Test
    void getAllCars_shouldReturn200WithListOfCars() throws Exception {
        List<Car> cars = List.of(
                Car.builder().id(1L).make("Toyota").model("Corolla").build(),
                Car.builder().id(2L).make("Honda").model("Civic").build()
        );
        when(getCarsUseCase.getCars()).thenReturn(cars);

        mockMvc.perform(get("/cars"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(cars)));

        verify(getCarsUseCase).getCars();
    }

    @Test
    void getAvailableCars_shouldReturn200WithAvailableCars_WhenRequestValid() throws Exception {
        GetAvailableCarsRequest request = GetAvailableCarsRequest.builder()
                .fromCity("Ein")
                .toCity("Ein")
                .fromDateTime(LocalDateTime.now())
                .toDateTime(LocalDateTime.now())
                .build();
        List<Car> availableCars = List.of(
                Car.builder().id(1L).make("Toyota").model("Corolla").build()
        );
        when(getAvailableCarsUseCase.getAvailableCars(request)).thenReturn(availableCars);

        mockMvc.perform(post("/cars/availableCars")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(availableCars)));

        verify(getAvailableCarsUseCase).getAvailableCars(request);
    }

    @Test
    void deleteCar_shouldReturn204_WhenCarDeleted() throws Exception {
        mockMvc.perform(delete("/cars/1"))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(deleteCarUseCase).deleteCar(1L);
    }

    @Test
    void createCar_shouldCreateAndReturn400_WhenRequestInValid() throws Exception {
        CreateCarRequest request = CreateCarRequest.builder()
                .make("A")
                .model("B")
                .registrationNumber("123")
                .features(null)
                .fuelConsumption(null)
                .cityId(null)
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        String expectedErrorResponse = """
            {
                "errors": [
                    {"field": "make", "error": "size must be between 2 and 50"},
                    {"field": "model", "error": "size must be between 2 and 50"},
                    {"field": "features", "error": "must not be empty"}
                ]
            }
            """;

        mockMvc.perform(post("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedErrorResponse, false));

        verifyNoInteractions(createCarUseCase);
    }

    @Test
    void createCar_shouldCreateAndReturn201_WhenRequestValid() throws Exception {
        CreateCarRequest request = CreateCarRequest.builder()
                .make("Toyota")
                .model("Corolla")
                .registrationNumber("ABC123")
                .features(List.of("GPS", "Airbags"))
                .fuelConsumption(10.0)
                .cityId(1L)
                .build();

        CreateCarResponse response = CreateCarResponse.builder()
                .carId(1L)
                .build();

        when(createCarUseCase.createCar(any(CreateCarRequest.class))).thenReturn(response);

        String requestJson = objectMapper.writeValueAsString(request);
        String responseJson = objectMapper.writeValueAsString(response);

        mockMvc.perform(post("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(responseJson, true));

        verify(createCarUseCase, times(1)).createCar(any(CreateCarRequest.class));
    }

    @Test
    void updateCar_shouldUpdateAndReturn400_WhenRequestInValid() throws Exception {
        UpdateCarRequest request = UpdateCarRequest.builder()
                .make("") // Invalid
                .model("") // Invalid
                .build(); // Missing fields like registrationNumber, fuelConsumption, and cityId.

        String requestJson = objectMapper.writeValueAsString(request);

        String expectedErrorResponse = """
            {
                "errors": [
                    {"field": "make", "error": "must not be blank"},
                    {"field": "registrationNumber", "error": "must not be blank"},
                    {"field": "model", "error": "must not be blank"}
                ]
            }
            """;

        mockMvc.perform(put("/cars/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedErrorResponse, false));

        verifyNoInteractions(updateCarUseCase);
    }

    @Test
    void updateCar_shouldUpdateAndReturn204_WhenRequestValid() throws Exception {
        UpdateCarRequest request = UpdateCarRequest.builder()
                .make("Toyota")
                .model("Corolla")
                .registrationNumber("ABC123")
                .fuelConsumption(8.0)
                .cityId(1L)
                .features(List.of("GPS", "Airbags"))
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/cars/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNoContent());

        verify(updateCarUseCase, times(1)).updateCar(any(UpdateCarRequest.class));
    }
}
