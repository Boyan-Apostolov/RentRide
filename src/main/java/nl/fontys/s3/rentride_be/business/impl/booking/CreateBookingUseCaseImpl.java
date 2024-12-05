package nl.fontys.s3.rentride_be.business.impl.booking;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.InvalidOperationException;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.use_cases.auction.UpdateAuctionCanBeClaimedUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.booking.CreateBookingUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.city.GetRouteBetweenCitiesUseCase;
import nl.fontys.s3.rentride_be.configuration.security.token.AccessToken;
import nl.fontys.s3.rentride_be.domain.auction.Auction;
import nl.fontys.s3.rentride_be.domain.booking.CreateBookingRequest;
import nl.fontys.s3.rentride_be.domain.booking.CreateBookingResponse;
import nl.fontys.s3.rentride_be.persistance.*;
import nl.fontys.s3.rentride_be.persistance.entity.*;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class CreateBookingUseCaseImpl implements CreateBookingUseCase {
    private BookingRepository bookingRepository;
    private CarRepository carRepository;
    private UserRepository userRepository;
    private CityRepository cityRepository;
    private AuctionRepository auctionRepository;
    private UpdateAuctionCanBeClaimedUseCase updateAuctionCanBeClaimedUseCase;
    private GetRouteBetweenCitiesUseCase routeBetweenCitiesUseCase;
    private AccessToken accessToken;

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
    private AuctionEntity tryGetAuction(Long auctionId){
        return auctionRepository.findById(auctionId)
                .orElseThrow(() -> new NotFoundException("CreateBooking->Auction"));
    }

    @Transactional
    @Override
    public CreateBookingResponse createBooking(CreateBookingRequest request) {
        CarEntity car = tryGetCar(request.getCarId());
        CityEntity fromCity = tryGetCity(request.getFromCityId());
        CityEntity toCity = tryGetCity(request.getToCityId());
        double distance = getDistance(fromCity.getId(), toCity.getId());
        BookingStatus bookingStatus = BookingStatus.Unpaid;

        if(request.getAuctionId() > 0){
        AuctionEntity auction = tryGetAuction(request.getAuctionId());
            if(!Objects.equals(auction.getWinnerUser().getId(), accessToken.getUserId()))
                throw new InvalidOperationException("Only the winner can claim the auction car!");

            request.setUserId(accessToken.getUserId());
            request.setCoverage(2);
            bookingStatus = BookingStatus.Paid;

            updateAuctionCanBeClaimedUseCase.updateState(request.getAuctionId(), 0);
        }

        UserEntity user = tryGetUser(request.getUserId());

        BookingEntity newBookingEntity = saveNewBooking(request, car, fromCity, toCity, user, distance, bookingStatus);

        return CreateBookingResponse.builder()
                .bookingId(newBookingEntity.getId())
                .booking(newBookingEntity)
                .build();
    }

    private BookingEntity saveNewBooking(CreateBookingRequest request, CarEntity car, CityEntity fromCity, CityEntity toCity, UserEntity user, double distance, BookingStatus bookingStatus) {
        BookingEntity bookingEntity = BookingEntity.builder()
                .status(bookingStatus)
                .coverage(BookingCoverage.values()[request.getCoverage()])
                .startDateTime(request.getStartDateTime())
                .endDateTime(request.getEndDateTime())
                .startCity(fromCity)
                .endCity(toCity)
                .car(car)
                .user(user)
                .distance(distance)
                .totalPrice(request.getTotalPrice())
                .paymentId("")
                .build();


        return this.bookingRepository.save(bookingEntity);
    }
}
