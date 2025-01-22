package nl.fontys.s3.rentride_be.business.impl.booking;

import lombok.RequiredArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.booking.GetBookingHistoryMapUseCase;
import nl.fontys.s3.rentride_be.domain.booking.Booking;
import nl.fontys.s3.rentride_be.domain.city.City;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetBookingHistoryMapUseCaseImpl implements GetBookingHistoryMapUseCase {
    @Value("${API_KEY_GEOAPIFY}")
    private String geoapifyApiKey;

    StringBuilder markers;
    StringBuilder polyline;

    @Override
    public String getBookingHistoryMap(List<Booking> bookings) {
        markers = new StringBuilder();
        polyline = new StringBuilder();

        List<Booking> sortedBookings = bookings.stream()
                .sorted(Comparator
                        .comparing((Booking b) -> b.getStartCity().getId())
                        .thenComparing(b -> b.getEndCity().getId()))
                .toList();

        sortedBookings.forEach(booking -> {
            City startCity = booking.getStartCity();
            City endCity = booking.getEndCity();

            markers.append(String.format("&marker=lonlat:%.6f,%.6f", startCity.getLon(), startCity.getLat()));
            markers.append(String.format("&marker=lonlat:%.6f,%.6f", endCity.getLon(), endCity.getLat()));

            if (polyline.length() > 0) {
                polyline.append(","); // Add a comma if polyline is not empty
            }
            polyline.append(String.format("%.6f,%.6f,%.6f,%.6f",
                    startCity.getLon(), startCity.getLat(),
                    endCity.getLon(), endCity.getLat()));
        });

        return String.format(
                "https://maps.geoapify.com/v1/staticmap?style=osm-bright&width=600&height=400"
                        + "%s"
                        + "&geometry=polyline:%s"
                        + "&apiKey=%s",
                markers.toString(),
                polyline.toString(),
                geoapifyApiKey
        );
    }
}
