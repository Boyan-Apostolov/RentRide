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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetBookingsForUserUseCaseImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private GetBookingsForUserUseCaseImpl getBookingsForUserUseCase;

    private BookingEntity bookingEntity1;
    private BookingEntity bookingEntity2;

    @BeforeEach
    void setUp() {
        bookingEntity1 = BookingEntity.builder()
                .id(1L)
                .startDateTime(LocalDateTime.of(2023, 11, 1, 10, 0))
                .endDateTime(LocalDateTime.of(2023, 11, 3, 10, 0))
                .totalPrice(200.0)
                .build();

        bookingEntity2 = BookingEntity.builder()
                .id(2L)
                .startDateTime(LocalDateTime.of(2023, 11, 4, 10, 0))
                .endDateTime(LocalDateTime.of(2023, 11, 6, 10, 0))
                .totalPrice(300.0)
                .build();
    }

    @Test
    void getBookingsForUser_ShouldReturnBookings_WhenBookingsExistForUser() {
        Long userId = 1L;
        when(bookingRepository.findByUserId(userId)).thenReturn(List.of(bookingEntity1, bookingEntity2));

        List<Booking> result = getBookingsForUserUseCase.getBookingsForUser(userId);

        assertEquals(2, result.size());
        assertEquals(bookingEntity1.getId(), result.get(0).getId());
        assertEquals(bookingEntity2.getId(), result.get(1).getId());

        verify(bookingRepository, times(1)).findByUserId(userId);
    }

    @Test
    void getBookingsForUser_ShouldReturnEmptyList_WhenNoBookingsExistForUser() {
        Long userId = 1L;
        when(bookingRepository.findByUserId(userId)).thenReturn(List.of());

        List<Booking> result = getBookingsForUserUseCase.getBookingsForUser(userId);

        assertEquals(0, result.size());

        verify(bookingRepository, times(1)).findByUserId(userId);
    }
}