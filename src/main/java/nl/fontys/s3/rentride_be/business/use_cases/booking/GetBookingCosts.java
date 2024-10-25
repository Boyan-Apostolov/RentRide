package nl.fontys.s3.rentride_be.business.use_cases.booking;

import nl.fontys.s3.rentride_be.domain.booking.GetBookingCostsResponse;

public interface GetBookingCosts {
    public GetBookingCostsResponse getBookingCosts(long carId, long fromCityId, long toCityId, long userId);
}
