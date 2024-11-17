package nl.fontys.s3.rentride_be.controller;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.booking.GetStatisticsUseCase;
import nl.fontys.s3.rentride_be.domain.statistics.GeneralStatisticsResponse;
import nl.fontys.s3.rentride_be.domain.statistics.StatisticsByCarResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("statistics")
@AllArgsConstructor
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
}
