package nl.fontys.s3.rentride_be.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.fontys.s3.rentride_be.business.useCases.city.*;
import nl.fontys.s3.rentride_be.domain.city.City;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    public void getCities_shouldReturn200WithEmptyListWhenNoCities() throws Exception {
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
    public void getCities_shouldReturn200WithFullListWhenPresentCities() throws Exception{
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
    public void getCity_shouldReturnOKWithContentWhenCityIsFound() throws Exception{
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
    public void getCity_shouldReturnNotFoundWithoutContentWhenCityIsNotFound() throws Exception {
        when(getCityUseCase.getCity(1L)).thenReturn(null);

        mockMvc
                .perform(get("/cities/1"))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(getCityUseCase).getCity(1L);
    }

}