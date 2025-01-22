package nl.fontys.s3.rentride_be.business.impl.booking;

import nl.fontys.s3.rentride_be.domain.booking.Booking;
import nl.fontys.s3.rentride_be.persistance.BookingRepository;
import nl.fontys.s3.rentride_be.persistance.entity.BookingEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetBookingByIdUseCaseImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private GetBookingByIdUseCaseImpl getBookingByIdUseCase;

    private BookingEntity bookingEntity;

    @BeforeEach
    void setUp() {
        bookingEntity = BookingEntity.builder()
                .id(1L)
                .startDateTime(LocalDateTime.of(2023, 11, 1, 10, 0))
                .endDateTime(LocalDateTime.of(2023, 11, 3, 10, 0))
                .totalPrice(200.0)
                .build();
    }

    @Test
    void getBookingById_ShouldReturnBooking_WhenBookingExists() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(bookingEntity));

        Booking result = getBookingByIdUseCase.getBookingById(1L);

        assertEquals(1L, result.getId());
        assertEquals(LocalDateTime.of(2023, 11, 1, 10, 0), result.getStartDateTime());
        assertEquals(LocalDateTime.of(2023, 11, 3, 10, 0), result.getEndDateTime());
        assertEquals(200.0, result.getTotalPrice());

        verify(bookingRepository, times(1)).findById(1L);
    }

    @Test
    void getBookingById_ShouldReturnNull_WhenBookingDoesNotExist() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        Booking result = getBookingByIdUseCase.getBookingById(1L);

        assertNull(result);
        verify(bookingRepository, times(1)).findById(1L);
    }
}