package nl.fontys.s3.rentride_be.controller;

import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.booking.GetStatisticsUseCase;
import nl.fontys.s3.rentride_be.domain.statistics.GeneralStatisticsResponse;
import nl.fontys.s3.rentride_be.domain.statistics.GroupingDto;
import nl.fontys.s3.rentride_be.domain.statistics.PopularCarOverTimeDto;
import nl.fontys.s3.rentride_be.domain.statistics.StatisticsByCarResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("statistics")
@AllArgsConstructor
@RolesAllowed({"ADMIN"})
public class StatisticsController {
    private GetStatisticsUseCase getBookingStatisticsUseCase;

    @GetMapping("by-car")
    public ResponseEntity<StatisticsByCarResponse> getReviews(@RequestParam(value = "carId") Long carId) {
        StatisticsByCarResponse statistics = this.getBookingStatisticsUseCase.getStatisticsByCar(carId);

        return ResponseEntity.ok().body(statistics);
    }

    @GetMapping("general")
    public ResponseEntity<GeneralStatisticsResponse> getGeneralStatistics() {
        GeneralStatisticsResponse statistics = this.getBookingStatisticsUseCase.getGeneralStatistics();

        return ResponseEntity.ok().body(statistics);
    }

    @GetMapping("discounts")
    public ResponseEntity<List<GroupingDto>> getMostBoughtDiscountPlans() {
        List<GroupingDto> mostBoughtPlans = this.getBookingStatisticsUseCase.getMostBoughtDiscountPlans();

        return ResponseEntity.ok().body(mostBoughtPlans);
    }

    @GetMapping("cars")
    public ResponseEntity<List<GroupingDto>> getMostPopularCars() {
        List<GroupingDto> mostPopularCars = this.getBookingStatisticsUseCase.getMostPopularCars();

        return ResponseEntity.ok().body(mostPopularCars);
    }

    @GetMapping("trips")
    public ResponseEntity<List<GroupingDto>> getMostPopularTrips() {
        List<GroupingDto> mostPopularTrips = this.getBookingStatisticsUseCase.getMostPopularTrips();

        return ResponseEntity.ok().body(mostPopularTrips);
    }

    @GetMapping("per-month")
    public ResponseEntity<List<GroupingDto>> getBookingsPerMonth() {
        List<GroupingDto> bookingsPerMonth = this.getBookingStatisticsUseCase.getBookingsPerMonth();

        return ResponseEntity.ok().body(bookingsPerMonth);
    }

    @GetMapping("popular-over-time")
    public ResponseEntity<List<PopularCarOverTimeDto>> getPopularCarsOverTime(@RequestParam(value = "startDateTime", required = false)
                                                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                              LocalDateTime startDateTime,

                                                                              @RequestParam(value = "endDateTime", required = false)
                                                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime endDateTime) {
        List<PopularCarOverTimeDto> bookingsPerMonth = this.getBookingStatisticsUseCase.getPopularCarsOverTime(startDateTime, endDateTime);

        return ResponseEntity.ok().body(bookingsPerMonth);
    }
}
