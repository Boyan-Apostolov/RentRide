package nl.fontys.s3.rentride_be.business.impl.booking;

import nl.fontys.s3.rentride_be.domain.booking.Booking;
import nl.fontys.s3.rentride_be.domain.city.City;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GetBookingHistoryMapUseCaseImplTest {

    private GetBookingHistoryMapUseCaseImpl getBookingHistoryMapUseCase;

    @BeforeEach
    void setUp() {
        getBookingHistoryMapUseCase = new GetBookingHistoryMapUseCaseImpl();
        ReflectionTestUtils.setField(getBookingHistoryMapUseCase, "geoapifyApiKey", "testApiKey");
    }

    @Test
    void getBookingHistoryMap_ShouldGenerateCorrectMapUrl() {
        City startCity1 = City.builder().id(1L).name("City A").lat(52.3676).lon(4.9041).build(); // Amsterdam
        City endCity1 = City.builder().id(2L).name("City B").lat(51.9244).lon(4.4777).build();   // Rotterdam
        Booking booking1 = Booking.builder().startCity(startCity1).endCity(endCity1).build();

        City startCity2 = City.builder().id(3L).name("City C").lat(51.4416).lon(5.4697).build(); // Eindhoven
        City endCity2 = City.builder().id(4L).name("City D").lat(50.8514).lon(5.6909).build();   // Maastricht
        Booking booking2 = Booking.builder().startCity(startCity2).endCity(endCity2).build();

        List<Booking> bookings = List.of(booking1, booking2);

        String mapUrl = getBookingHistoryMapUseCase.getBookingHistoryMap(bookings);

        String expectedMarkers = "&marker=lonlat:4.904100,52.367600"
                + "&marker=lonlat:4.477700,51.924400"
                + "&marker=lonlat:5.469700,51.441600"
                + "&marker=lonlat:5.690900,50.851400";
        String expectedPolyline = "4.904100,52.367600,4.477700,51.924400,5.469700,51.441600,5.690900,50.851400";
        String expectedUrl = String.format(
                "https://maps.geoapify.com/v1/staticmap?style=osm-bright&width=600&height=400%s&geometry=polyline:%s&apiKey=%s",
                expectedMarkers,
                expectedPolyline,
                "testApiKey"
        );

        assertEquals(expectedUrl, mapUrl);
    }

    @Test
    void getBookingHistoryMap_ShouldReturnEmptyMap_WhenNoBookings() {
        String mapUrl = getBookingHistoryMapUseCase.getBookingHistoryMap(List.of());

        String expectedUrl = "https://maps.geoapify.com/v1/staticmap?style=osm-bright&width=600&height=400&geometry=polyline:&apiKey=testApiKey";

        assertEquals(expectedUrl, mapUrl);
    }
}