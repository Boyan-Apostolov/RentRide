package nl.fontys.s3.rentride_be.business.impl.booking;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.booking.UpdateBookingUseCase;
import nl.fontys.s3.rentride_be.persistance.BookingRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdateBookingUseCaseImpl implements UpdateBookingUseCase {
    private BookingRepository bookingRepository;
}
