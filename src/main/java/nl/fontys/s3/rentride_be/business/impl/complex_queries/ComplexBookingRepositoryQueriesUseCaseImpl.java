package nl.fontys.s3.rentride_be.business.impl.complex_queries;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.complex_queries.ComplexBookingRepositoryQueriesUseCase;
import nl.fontys.s3.rentride_be.persistance.BookingRepository;
import nl.fontys.s3.rentride_be.persistance.entity.BookingEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ComplexBookingRepositoryQueriesUseCaseImpl implements ComplexBookingRepositoryQueriesUseCase {
private BookingRepository bookingRepository;

    public Double sumDistanceByCarId(Long carId) {
        return bookingRepository.findAllByCarId(carId).stream()
                .mapToDouble(BookingEntity::getDistance)
                .sum();
    }

    public Double sumDistances() {
        return bookingRepository.findAll().stream()
                .mapToDouble(BookingEntity::getDistance)
                .sum();
    }

    public Double sumPricesByCarId(Long carId) {
        return bookingRepository.findAllByCarId(carId).stream()
                .mapToDouble(BookingEntity::getTotalPrice)
                .sum();
    }

    public Double sumPrices() {
        return bookingRepository.findAll().stream()
                .mapToDouble(BookingEntity::getTotalPrice)
                .sum();
    }
}
