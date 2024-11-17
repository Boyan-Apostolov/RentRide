package nl.fontys.s3.rentride_be.business.impl.complex_queries;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.complex_queries.ComplexBookingRepositoryQueriesUseCase;
import nl.fontys.s3.rentride_be.domain.statistics.GroupingDto;
import nl.fontys.s3.rentride_be.persistance.BookingRepository;
import nl.fontys.s3.rentride_be.persistance.entity.BookingEntity;
import org.springframework.stereotype.Service;

import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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

    @Override
    public List<GroupingDto> getMostPopularCars() {
        return this.bookingRepository
                .findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        booking -> String.format("%s %s", booking.getCar().getMake(), booking.getCar().getModel()),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .map(entry -> new GroupingDto(entry.getKey(), entry.getValue()))
                .toList();
    }

    @Override
    public List<GroupingDto> getMostPopularTrips() {
        return this.bookingRepository
                .findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        booking -> String.format("%s -> %s", booking.getStartCity().getName(), booking.getEndCity().getName()),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .map(entry -> new GroupingDto(entry.getKey(), entry.getValue()))
                .toList();
    }

    @Override
    public List<GroupingDto> getBookingsPerMonth() {
        return this.bookingRepository
                .findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        booking -> booking.getStartDateTime().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .map(entry -> new GroupingDto(entry.getKey(), entry.getValue()))
                .toList();
    }
}
