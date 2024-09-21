package nl.fontys.s3.rentride_be.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.useCases.city.*;
import nl.fontys.s3.rentride_be.domain.city.City;
import nl.fontys.s3.rentride_be.domain.city.CreateCityRequest;
import nl.fontys.s3.rentride_be.domain.city.CreateCityResponse;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CitiesController.class)
class CitiesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetCityUseCase getCityUseCase;

    @MockBean
    private GetCitiesUseCase getCitiesUseCase;

    @MockBean
    private DeleteCityUseCase deleteCityUseCase;

    @MockBean
    private UpdateCityUseCase updateCityUseCase;

    @MockBean
    private CreateCityUseCase createCityUseCase;

    @MockBean
    private LookupCityUseCase lookupCityUseCase;


    @Test
    public void getCities_shouldReturn200WithEmptyList_WhenNoCities() throws Exception {
        List<City> expectedCities = new ArrayList<>();

        when(getCitiesUseCase.getCities()).thenReturn(expectedCities);

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(expectedCities);

        mockMvc
                .perform(get("/cities"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json(expectedJson));

        verify(getCitiesUseCase).getCities();

    }

    @Test
    public void getCities_shouldReturn200WithFullList_WhenPresentCities() throws Exception {
        List<City> expectedCities = List.of(
                City.builder()
                        .id(1L)
                        .name("Eindhoven")
                        .lat(12.2)
                        .lon(12.2)
                        .build(),
                City.builder()
                        .id(2L)
                        .name("Amsterdam")
                        .lat(13.2)
                        .lon(13.2)
                        .build()
        );

        when(getCitiesUseCase.getCities()).thenReturn(expectedCities);

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(expectedCities);

        mockMvc
                .perform(get("/cities"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json(expectedJson));

        verify(getCitiesUseCase).getCities();
    }

    @Test
    public void getCity_shouldReturn200WithContent_WhenCityIsFound() throws Exception {
        City expectedCity = City.builder()
                .id(1L)
                .name("Eindhoven")
                .lat(12.2)
                .lon(12.2)
                .build();

        when(getCityUseCase.getCity(1L)).thenReturn(expectedCity);

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(expectedCity);

        mockMvc
                .perform(get("/cities/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json(expectedJson));

        verify(getCityUseCase).getCity(1L);
    }

    @Test
    public void getCity_shouldReturn400WithoutContent_WhenCityIsNotFound() throws Exception {
        when(getCityUseCase.getCity(1L)).thenReturn(null);

        mockMvc
                .perform(get("/cities/1"))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(getCityUseCase).getCity(1L);
    }

    @Test
    public void createCity_shouldCreateAndReturn201_WhenRequestValid() throws Exception {
        CreateCityRequest expectedCity = CreateCityRequest.builder()
                .name("Eindhoven")
                .lat(12.12)
                .lon(12.12)
                .build();

        CreateCityResponse expectedResponse = CreateCityResponse
                .builder()
                .cityId(1L)
                .build();

        when(createCityUseCase.createCity(expectedCity))
                .thenReturn(expectedResponse);

        ObjectMapper objectMapper = new ObjectMapper();

        String sampleRequestJson = objectMapper.writeValueAsString(expectedCity);
        String expectedResponseJson = objectMapper.writeValueAsString(expectedResponse);

        mockMvc.perform(post("/cities")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(sampleRequestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json(expectedResponseJson));

        verify(createCityUseCase).createCity(expectedCity);
    }

    @Test
    public void createCity_shouldCreateAndReturn400_WhenRequestInValid() throws Exception {
        CreateCityRequest expectedCity = CreateCityRequest.builder()
                .name("Eindhoven2")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();

        String sampleRequestJson = objectMapper.writeValueAsString(expectedCity);
        String expectedErrorResponse = """
                        {
                    "errors": [
                        {"field": "lon", "error": "must not be null"},
                        {"field": "lat", "error": "must not be null"}
                    ]
                }
                """;

        mockMvc.perform(post("/cities")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(sampleRequestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedErrorResponse, false));

        verifyNoInteractions(createCityUseCase);
    }
    
    @Test
    public void deleteCity_shouldReturnNoContent_WhenCityIsFound() throws Exception {
        mockMvc.perform(delete("/cities/1")
                        .contentType(APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(deleteCityUseCase).deleteCity(1L);
    }

    @Test
    public void deleteCity_shouldThrowException_WhenCityIsNotFound() throws Exception {
        doThrow(new NotFoundException("Delete->City")).when(deleteCityUseCase).deleteCity(1L);

        String expectedErrorResponse = """
                {
                 "errors": [
                         {
                             "field": null,
                             "error": "Delete->City_NOT_FOUND"
                         }
                     ]
                }""";
        mockMvc.perform(delete("/cities/1")
                        .contentType(APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedErrorResponse, false));

        verify(deleteCityUseCase).deleteCity(1L);
    }
}