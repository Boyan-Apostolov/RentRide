package nl.fontys.s3.rentride_be.controller;

import nl.fontys.s3.rentride_be.business.use_cases.booking.*;
import nl.fontys.s3.rentride_be.business.use_cases.damage.GetAllDamageUseCase;
import nl.fontys.s3.rentride_be.domain.booking.*;
import nl.fontys.s3.rentride_be.domain.damage.Damage;
import nl.fontys.s3.rentride_be.persistance.entity.BookingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingsControllerTest {

    @Mock
    private GetBookingsUseCase getBookingsUseCase;
    @Mock
    private GetBookingCosts getBookingCostsUseCase;
    @Mock
    private CreateBookingUseCase createBookingUseCase;
    @Mock
    private GetBookingsForUserUseCase getBookingsForUserUseCase;
    @Mock
    private GetBookingsForCarUseCase getBookingsForCarUseCase;
    @Mock
    private UpdateBookingStatusUseCase updateBookingStatusUseCase;
    @Mock
    private GetAllDamageUseCase getAllDamageUseCase;
    @Mock
    private GetBookingHistoryMapUseCase getBookingHistoryMapUseCase;

    @InjectMocks
    private BookingsController bookingsController;

    private Booking booking;
    private Damage damage;
    private CreateBookingRequest createBookingRequest;
    private CreateBookingResponse createBookingResponse;
    private GetBookingCostsResponse bookingCostsResponse;

    @BeforeEach
    void setUp() {
        booking = Booking.builder().id(1L).build();
        damage = Damage.builder().id(1L).name("Scratch").build();
        createBookingRequest = CreateBookingRequest.builder().userId(1L).carId(2L).build();
        createBookingResponse = CreateBookingResponse.builder().bookingId(1L).build();
        bookingCostsResponse = GetBookingCostsResponse.builder().fuelCost(10.0).build();
    }

    @Test
    void getBookings_ShouldReturnAllBookings() {
        when(getBookingsUseCase.getBookings()).thenReturn(List.of(booking));

        ResponseEntity<List<Booking>> response = bookingsController.getBookings();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(getBookingsUseCase, times(1)).getBookings();
    }

    @Test
    void getPossibleDamages_ShouldReturnAllDamages() {
        when(getAllDamageUseCase.getAllDamage()).thenReturn(List.of(damage));

        ResponseEntity<List<Damage>> response = bookingsController.getPossibleDamages();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(getAllDamageUseCase, times(1)).getAllDamage();
    }

    @Test
    void getUserBookings_ShouldReturnUserBookings() {
        when(getBookingsForUserUseCase.getBookingsForUser()).thenReturn(List.of(booking));

        ResponseEntity<List<Booking>> response = bookingsController.getUserBookings();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(getBookingsForUserUseCase, times(1)).getBookingsForUser();
    }

    @Test
    void getCarBookings_ShouldReturnCarBookings() {
        when(getBookingsForCarUseCase.getBookings(2L)).thenReturn(List.of(booking));

        ResponseEntity<List<Booking>> response = bookingsController.getCarBookings(2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(getBookingsForCarUseCase, times(1)).getBookings(2L);
    }

    @Test
    void cancelBooking_ShouldUpdateBookingStatusToCanceled() {
        doNothing().when(updateBookingStatusUseCase).updateBookingStatus(1L, BookingStatus.Canceled);

        ResponseEntity<Void> response = bookingsController.cancelBooking(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(updateBookingStatusUseCase, times(1)).updateBookingStatus(1L, BookingStatus.Canceled);
    }

    @Test
    void getBookingCosts_ShouldReturnBookingCosts() {
        // Use a fixed LocalDateTime for consistency
        LocalDateTime now = LocalDateTime.now();

        GetBookingCostsRequest request = GetBookingCostsRequest.builder()
                .carId(2L)
                .fromCityId(1L)
                .toCityId(3L)
                .fromDateTime(now)
                .toDateTime(now)
                .build();

        when(getBookingCostsUseCase.getBookingCosts(request)).thenReturn(bookingCostsResponse);

        GetBookingCostsResponse response = bookingsController.getBookingCosts(request);

        assertEquals(10.0, response.getFuelCost());
        verify(getBookingCostsUseCase, times(1)).getBookingCosts(request);
    }

    @Test
    void createBooking_ShouldReturnCreatedBookingResponse() {
        when(createBookingUseCase.createBooking(createBookingRequest)).thenReturn(createBookingResponse);

        ResponseEntity<CreateBookingResponse> response = bookingsController.createBooking(createBookingRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createBookingResponse, response.getBody());
        verify(createBookingUseCase, times(1)).createBooking(createBookingRequest);
    }

    @Test
    void getCarBookingsMap_ShouldReturnMapUrl() {
        when(getBookingsForCarUseCase.getBookings(2L)).thenReturn(List.of(booking));
        when(getBookingHistoryMapUseCase.getBookingHistoryMap(anyList())).thenReturn("http://map-url.com");

        ResponseEntity<String> response = bookingsController.getCarBookingsMap(2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("http://map-url.com", response.getBody());
        verify(getBookingsForCarUseCase, times(1)).getBookings(2L);
        verify(getBookingHistoryMapUseCase, times(1)).getBookingHistoryMap(anyList());
    }
}