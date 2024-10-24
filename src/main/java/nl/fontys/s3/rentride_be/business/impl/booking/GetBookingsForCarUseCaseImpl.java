package nl.fontys.s3.rentride_be.business.impl.booking;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.useCases.booking.GetBookingsForCarUseCase;
import nl.fontys.s3.rentride_be.domain.booking.Booking;
import nl.fontys.s3.rentride_be.persistance.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetBookingsForCarUseCaseImpl implements GetBookingsForCarUseCase {
    private BookingRepository bookingRepository;

    @Override
    public List<Booking> getBookings(Long carId) {
        return bookingRepository.findBookingsByCarIdOrderByStartDateTime(carId)
                        .stream()
                        .map(BookingConverter::convert)
                        .toList();
    }
}
