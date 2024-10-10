package nl.fontys.s3.rentride_be.business.impl.booking;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.useCases.booking.GetBookingsForUserUseCase;
import nl.fontys.s3.rentride_be.persistance.BookingRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetBookingsForUserUseCaseImpl implements GetBookingsForUserUseCase {
    private BookingRepository bookingRepository;
}
