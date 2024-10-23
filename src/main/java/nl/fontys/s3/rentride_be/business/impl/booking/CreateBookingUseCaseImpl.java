package nl.fontys.s3.rentride_be.business.impl.booking;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.useCases.booking.CreateBookingUseCase;
import nl.fontys.s3.rentride_be.business.useCases.city.GetRouteBetweenCitiesUseCase;
import nl.fontys.s3.rentride_be.domain.booking.CreateBookingRequest;
import nl.fontys.s3.rentride_be.domain.booking.CreateBookingResponse;
import nl.fontys.s3.rentride_be.persistance.BookingRepository;
import nl.fontys.s3.rentride_be.persistance.CarRepository;
import nl.fontys.s3.rentride_be.persistance.CityRepository;
import nl.fontys.s3.rentride_be.persistance.UserRepository;
import nl.fontys.s3.rentride_be.persistance.entity.*;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateBookingUseCaseImpl implements CreateBookingUseCase {
    private BookingRepository bookingRepository;
    private CarRepository carRepository;
    private UserRepository userRepository;
    private CityRepository cityRepository;
    private GetRouteBetweenCitiesUseCase routeBetweenCitiesUseCase;

    private CityEntity tryGetCity(Long cityId) {
        return cityRepository.findById(cityId)
                .orElseThrow(() -> new NotFoundException("CreateBooking->City"));
    }

    private CarEntity tryGetCar(Long carId) {
        return carRepository.findById(carId)
                .orElseThrow(() -> new NotFoundException("CreateBooking->Car"));
    }

    private UserEntity tryGetUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("CreateBooking->User"));
    }
    private double getDistance(Long fromCityId, Long toCityId) {
        return Double.parseDouble(
                routeBetweenCitiesUseCase.getRoute(fromCityId, toCityId).getDistance()
        );
    }

    @Override
    public CreateBookingResponse createBooking(CreateBookingRequest request) {
        UserEntity user = tryGetUser(request.getUserId());
        CarEntity car = tryGetCar(request.getCarId());
        CityEntity fromCity = tryGetCity(request.getFromCityId());
        CityEntity toCity = tryGetCity(request.getToCityId());
        Double distance = getDistance(fromCity.getId(), toCity.getId());

        BookingEntity newBookingEntity = saveNewBooking(request, car, fromCity, toCity, user, distance);

        return CreateBookingResponse.builder()
                .bookingId(newBookingEntity.getId())
                .build();
    }

    private BookingEntity saveNewBooking(CreateBookingRequest request, CarEntity car, CityEntity fromCity, CityEntity toCity, UserEntity user, double distance) {
        BookingEntity bookingEntity = BookingEntity.builder()
                .status(BookingStatus.Unpaid)
                .coverage(BookingCoverage.values()[request.getCoverage()])
                .startDateTime(request.getStartDateTime())
                .endDateTime(request.getEndDateTime())
                .startCity(fromCity)
                .endCity(toCity)
                .car(car)
                .user(user)
                .distance(distance)
                .totalPrice(request.getTotalPrice())
                .build();


        return this.bookingRepository.save(bookingEntity);
    }
}
