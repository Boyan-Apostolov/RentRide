package nl.fontys.s3.rentride_be.domain.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatisticsByCarResponse {
    private Long totalBookings;
    private Double totalRevenue;
    private Double totalDistance;
    private Double averageRating;
}
