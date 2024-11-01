package nl.fontys.s3.rentride_be.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
@WebMvcTest(CarsController.class)
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
    void createCar_shouldReturn201_WhenRequestValid() throws Exception {
        CreateCarRequest request = CreateCarRequest.builder()
                .make("Toyota")
                .model("Corolla")
                .registrationNumber("ABC123")
                .build();
        CreateCarResponse response = CreateCarResponse.builder().carId(1L).build();

        when(createCarUseCase.createCar(request)).thenReturn(response);

        mockMvc.perform(post("/cars")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(createCarUseCase).createCar(request);
    }

    @Test
    void updateCar_shouldReturn204_WhenRequestValid() throws Exception {
        UpdateCarRequest request = UpdateCarRequest.builder()
                .id(1L)
                .make("Toyota")
                .model("UpdatedModel")
                .registrationNumber("ABC123")
                .build();

        mockMvc.perform(put("/cars/1")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(updateCarUseCase).updateCar(request);
    }
}
