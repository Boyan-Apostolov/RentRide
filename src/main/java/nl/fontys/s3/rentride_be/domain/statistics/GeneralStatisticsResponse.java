package nl.fontys.s3.rentride_be.domain.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeneralStatisticsResponse {
    private Long totalBookings;
    private Double totalRevenue;
    private Double totalTravelDistance;
    private Long totalUsers;
    private Long totalReviews;
}
