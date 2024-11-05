package nl.fontys.s3.rentride_be.business.impl.booking;

import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.use_cases.booking.GetBookingsForCarUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.booking.ScheduleBookingJobsUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.car.MoveCarUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.payment.RefundPaymentUseCase;
import nl.fontys.s3.rentride_be.persistance.BookingRepository;
import nl.fontys.s3.rentride_be.persistance.entity.BookingEntity;
import nl.fontys.s3.rentride_be.persistance.entity.BookingStatus;
import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
import nl.fontys.s3.rentride_be.persistance.entity.CityEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateBookingStatusUseCaseImplTest {

    @InjectMocks
    private UpdateBookingStatusUseCaseImpl updateBookingStatusUseCase;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private GetBookingsForCarUseCase getBookingsForCarUseCase;

    @Mock
    private MoveCarUseCase moveCarUseCase;

    @Mock
    private ScheduleBookingJobsUseCase scheduleBookingJobsUseCase;

    @Mock
    private RefundPaymentUseCase refundPaymentUseCase;

    private BookingEntity mockBookingEntity;
    private CarEntity car;
    private CityEntity currentCity;
    private CityEntity startCity;

    @BeforeEach
     void setUp() {
        currentCity = new CityEntity();
        currentCity.setId(2L);

        startCity = new CityEntity();
        startCity.setId(1L);

        car = new CarEntity();
        car.setId(1L);
        car.setCity(currentCity);

        mockBookingEntity = new BookingEntity();
        mockBookingEntity.setId(1L);
        mockBookingEntity.setCar(car);
        mockBookingEntity.setStatus(BookingStatus.Unpaid);
        mockBookingEntity.setStartCity(startCity);
    }

    @Test
     void testUpdateBookingStatus_NotFoundException() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                updateBookingStatusUseCase.updateBookingStatus(1L, BookingStatus.Paid));

        verify(bookingRepository, times(1)).findById(1L);
    }

    @Test
     void testUpdateBookingStatus_ValidTransition() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(mockBookingEntity));

        updateBookingStatusUseCase.updateBookingStatus(1L, BookingStatus.Paid);

        verify(bookingRepository, times(1)).save(mockBookingEntity);
        verify(scheduleBookingJobsUseCase, never()).cancelJobsByBookingId(anyLong());
        verify(refundPaymentUseCase, never()).refundPayment(anyString());
    }

    @Test
     void testUpdateBookingStatus_InvalidTransition() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(mockBookingEntity));

        updateBookingStatusUseCase.updateBookingStatus(1L, BookingStatus.Finished);

        verify(bookingRepository, never()).save(mockBookingEntity);
        verify(scheduleBookingJobsUseCase, never()).cancelJobsByBookingId(anyLong());
    }

    @Test
     void testUpdateBookingStatus_CanceledStatus() {
        mockBookingEntity.setStatus(BookingStatus.Paid);
        mockBookingEntity.setPaymentId("payment_123");
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(mockBookingEntity));

        updateBookingStatusUseCase.updateBookingStatus(1L, BookingStatus.Canceled);

        verify(refundPaymentUseCase, times(1)).refundPayment("payment_123");
        verify(scheduleBookingJobsUseCase, times(1)).cancelJobsByBookingId(mockBookingEntity.getId());
    }

    @Test
     void testUpdateBookingStatus_ActiveStatusWithCarMove() {
        mockBookingEntity.setStatus(BookingStatus.Paid);
        car.setCity(currentCity);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(mockBookingEntity));

        updateBookingStatusUseCase.updateBookingStatus(1L, BookingStatus.Active);

        verify(moveCarUseCase, times(1)).moveCar(car.getId(), startCity.getId());
    }

    @Test
     void testSetBookingPaymentId_NotFound() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                updateBookingStatusUseCase.setBookingPaymentId(1L, "payment_123"));
    }

    @Test
    void testSetBookingPaymentId_Success() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(mockBookingEntity));

        updateBookingStatusUseCase.setBookingPaymentId(1L, "payment_123");

        assertEquals("payment_123", mockBookingEntity.getPaymentId());

        verify(bookingRepository, times(1)).save(mockBookingEntity);
    }

    @Test
     void testUpdateBookingStatus_FinishedStatusWithCarMove() {
        mockBookingEntity.setStatus(BookingStatus.Active);
        car.setCity(currentCity);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(mockBookingEntity));
        when(getBookingsForCarUseCase.getBookings(car.getId())).thenReturn(List.of());

        updateBookingStatusUseCase.updateBookingStatus(1L, BookingStatus.Finished);

        verify(moveCarUseCase, never()).moveCar(anyLong(), anyLong());
    }
}