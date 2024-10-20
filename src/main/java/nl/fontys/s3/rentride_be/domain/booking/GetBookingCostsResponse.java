package nl.fontys.s3.rentride_be.domain.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GetBookingCostsResponse {
    private double fuelCost;
    private double serviceFees;
    private double tollFees;
    private double userDiscount;
    private double coverageFee;
    private double total;
    private double newTotal = total;
}
