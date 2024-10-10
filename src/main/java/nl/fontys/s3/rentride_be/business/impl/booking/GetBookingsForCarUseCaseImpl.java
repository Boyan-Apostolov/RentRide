package nl.fontys.s3.rentride_be.business.impl.booking;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.useCases.booking.GetBookingsForCarUseCase;
import nl.fontys.s3.rentride_be.persistance.BookingRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetBookingsForCarUseCaseImpl implements GetBookingsForCarUseCase {
    private BookingRepository bookingRepository;
}
