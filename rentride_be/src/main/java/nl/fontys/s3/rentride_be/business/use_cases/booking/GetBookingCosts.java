package nl.fontys.s3.rentride_be.business.use_cases.booking;

import nl.fontys.s3.rentride_be.domain.booking.GetBookingCostsRequest;
import nl.fontys.s3.rentride_be.domain.booking.GetBookingCostsResponse;

public interface GetBookingCosts {
    public GetBookingCostsResponse getBookingCosts(GetBookingCostsRequest request);
}
