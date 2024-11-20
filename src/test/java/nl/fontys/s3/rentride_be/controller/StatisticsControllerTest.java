package nl.fontys.s3.rentride_be.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.fontys.s3.rentride_be.business.use_cases.booking.GetStatisticsUseCase;
import nl.fontys.s3.rentride_be.domain.statistics.GeneralStatisticsResponse;
import nl.fontys.s3.rentride_be.domain.statistics.GroupingDto;
import nl.fontys.s3.rentride_be.domain.statistics.StatisticsByCarResponse;
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

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "10@fontys.nl", roles = {"ADMIN"})
class StatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetStatisticsUseCase getBookingStatisticsUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getReviews_ShouldReturnStatisticsByCar() throws Exception {
        Long carId = 1L;
        StatisticsByCarResponse mockResponse = StatisticsByCarResponse.builder()
                .totalDistance(500.0)
                .totalRevenue(1000.0)
                .averageRating(4.5)
                .totalBookings(10L)
                .build();

        when(getBookingStatisticsUseCase.getStatisticsByCar(carId)).thenReturn(mockResponse);

        mockMvc.perform(get("/statistics/by-car")
                        .param("carId", carId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockResponse)));

        verify(getBookingStatisticsUseCase, times(1)).getStatisticsByCar(carId);
    }

    @Test
    void getGeneralStatistics_ShouldReturnGeneralStatistics() throws Exception {
        GeneralStatisticsResponse mockResponse = GeneralStatisticsResponse.builder()
                .totalBookings(100L)
                .totalRevenue(20000.0)
                .totalTravelDistance(15000.0)
                .totalUsers(50L)
                .totalReviews(25L)
                .build();

        when(getBookingStatisticsUseCase.getGeneralStatistics()).thenReturn(mockResponse);

        mockMvc.perform(get("/statistics/general")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockResponse)));

        verify(getBookingStatisticsUseCase, times(1)).getGeneralStatistics();
    }

    @Test
    void getMostBoughtDiscountPlans_ShouldReturnGroupingDtoList() throws Exception {
        List<GroupingDto> mockResponse = List.of(
                new GroupingDto("Plan A", 5L),
                new GroupingDto("Plan B", 3L)
        );

        when(getBookingStatisticsUseCase.getMostBoughtDiscountPlans()).thenReturn(mockResponse);

        mockMvc.perform(get("/statistics/discounts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockResponse)));

        verify(getBookingStatisticsUseCase, times(1)).getMostBoughtDiscountPlans();
    }

    @Test
    void getMostPopularCars_ShouldReturnGroupingDtoList() throws Exception {
        List<GroupingDto> mockResponse = List.of(
                new GroupingDto("Car A", 10L),
                new GroupingDto("Car B", 7L)
        );

        when(getBookingStatisticsUseCase.getMostPopularCars()).thenReturn(mockResponse);

        mockMvc.perform(get("/statistics/cars")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockResponse)));

        verify(getBookingStatisticsUseCase, times(1)).getMostPopularCars();
    }

    @Test
    void getMostPopularTrips_ShouldReturnGroupingDtoList() throws Exception {
        List<GroupingDto> mockResponse = List.of(
                new GroupingDto("City A -> City B", 12L),
                new GroupingDto("City C -> City D", 8L)
        );

        when(getBookingStatisticsUseCase.getMostPopularTrips()).thenReturn(mockResponse);

        mockMvc.perform(get("/statistics/trips")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockResponse)));

        verify(getBookingStatisticsUseCase, times(1)).getMostPopularTrips();
    }

    @Test
    void getBookingsPerMonth_ShouldReturnGroupingDtoList() throws Exception {
        List<GroupingDto> mockResponse = List.of(
                new GroupingDto("January", 15L),
                new GroupingDto("February", 10L)
        );

        when(getBookingStatisticsUseCase.getBookingsPerMonth()).thenReturn(mockResponse);

        mockMvc.perform(get("/statistics/per-month")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockResponse)));

        verify(getBookingStatisticsUseCase, times(1)).getBookingsPerMonth();
    }
}