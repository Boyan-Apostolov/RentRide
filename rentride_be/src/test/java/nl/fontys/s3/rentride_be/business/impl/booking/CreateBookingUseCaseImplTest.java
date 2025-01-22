package nl.fontys.s3.rentride_be.business.impl.booking;

import nl.fontys.s3.rentride_be.business.exception.InvalidOperationException;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.use_cases.auction.UpdateAuctionCanBeClaimedUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.city.GetRouteBetweenCitiesUseCase;
import nl.fontys.s3.rentride_be.configuration.security.token.AccessToken;
import nl.fontys.s3.rentride_be.domain.booking.CreateBookingRequest;
import nl.fontys.s3.rentride_be.domain.booking.CreateBookingResponse;
import nl.fontys.s3.rentride_be.domain.city.GetRouteResponse;
import nl.fontys.s3.rentride_be.persistance.*;
import nl.fontys.s3.rentride_be.persistance.entity.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateBookingUseCaseImplTest {
    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private UpdateAuctionCanBeClaimedUseCase updateAuctionCanBeClaimedUseCase;

    @Mock
    private GetRouteBetweenCitiesUseCase routeBetweenCitiesUseCase;

    @Mock
    private AccessToken accessToken;

    @InjectMocks
    private CreateBookingUseCaseImpl createBookingUseCase;

    @Test
    void createBooking_shouldReturnResponse_whenRequestIsValid() {
        CreateBookingRequest request = CreateBookingRequest.builder()
                .carId(1L)
                .fromCityId(1L)
                .toCityId(2L)
                .userId(1L)
                .startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now().plusDays(2))
                .totalPrice(100.0)
                .coverage(0)
                .auctionId(0L)
                .build();

        CarEntity car = CarEntity.builder().id(1L).build();
        CityEntity fromCity = CityEntity.builder().id(1L).build();
        CityEntity toCity = CityEntity.builder().id(2L).build();
        UserEntity user = UserEntity.builder().id(1L).build();
        BookingEntity booking = BookingEntity.builder().id(1L).build();

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(cityRepository.findById(1L)).thenReturn(Optional.of(fromCity));
        when(cityRepository.findById(2L)).thenReturn(Optional.of(toCity));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(routeBetweenCitiesUseCase.getRoute(1L, 2L)).thenReturn(
                new GetRouteResponse("100.0", "1h", "")        );
        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(booking);

        CreateBookingResponse response = createBookingUseCase.createBooking(request);

        assertThat(response).isNotNull();
        assertThat(response.getBookingId()).isEqualTo(1L);
        verify(bookingRepository).save(any(BookingEntity.class));
    }

    @Test
    void createBooking_shouldThrowNotFoundException_whenCarNotFound() {
        CreateBookingRequest request = CreateBookingRequest.builder()
                .carId(1L)
                .fromCityId(1L)
                .toCityId(2L)
                .build();

        when(carRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> createBookingUseCase.createBooking(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("400 BAD_REQUEST \"CreateBooking->Car_NOT_FOUND\"");
    }

    @Test
    void createBooking_shouldThrowInvalidOperationException_whenUserIsNotAuctionWinner() {
        CreateBookingRequest request = CreateBookingRequest.builder()
                .carId(1L)
                .fromCityId(1L)
                .toCityId(2L)
                .auctionId(1L)
                .build();

        AuctionEntity auction = AuctionEntity.builder().id(1L).winnerUser(UserEntity.builder().id(2L).build()).build();

        when(routeBetweenCitiesUseCase.getRoute(1L, 2L)).thenReturn(
                new GetRouteResponse("100.0", "1h", "")
        );
        when(carRepository.findById(1L)).thenReturn(Optional.of(CarEntity.builder().id(1L).build()));
        when(cityRepository.findById(1L)).thenReturn(Optional.of(CityEntity.builder().id(1L).build()));
        when(cityRepository.findById(2L)).thenReturn(Optional.of(CityEntity.builder().id(2L).build()));
        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));
        when(accessToken.getUserId()).thenReturn(1L);

        assertThatThrownBy(() -> createBookingUseCase.createBooking(request))
                .isInstanceOf(InvalidOperationException.class)
                .hasMessage("400 BAD_REQUEST \"Only the winner can claim the auction car!\"");
    }

    @Test
    void createBooking_shouldCallUpdateAuctionState_whenAuctionIdProvided() {
        CreateBookingRequest request = CreateBookingRequest.builder()
                .carId(1L)
                .fromCityId(1L)
                .toCityId(2L)
                .userId(1L)
                .auctionId(1L)
                .startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now())
                .coverage(1)
                .build();

        BookingEntity booking = BookingEntity.builder().id(1L).build();
        AuctionEntity auction = AuctionEntity.builder().id(1L).winnerUser(UserEntity.builder().id(1L).build()).build();
        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(booking);
        when(carRepository.findById(1L)).thenReturn(Optional.of(CarEntity.builder().id(1L).build()));
        when(cityRepository.findById(1L)).thenReturn(Optional.of(CityEntity.builder().id(1L).build()));
        when(cityRepository.findById(2L)).thenReturn(Optional.of(CityEntity.builder().id(2L).build()));
        when(userRepository.findById(1L)).thenReturn(Optional.of(UserEntity.builder().id(1L).build()));
        when(routeBetweenCitiesUseCase.getRoute(1L, 2L)).thenReturn(
                new GetRouteResponse("100.0", "1h", "")
        );
        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));
        when(accessToken.getUserId()).thenReturn(1L);

        createBookingUseCase.createBooking(request);

        verify(updateAuctionCanBeClaimedUseCase).updateState(1L, 0);
    }
}