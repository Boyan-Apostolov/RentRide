package nl.fontys.s3.rentride_be.business.impl.booking;

import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.use_cases.city.GetRouteBetweenCitiesUseCase;
import nl.fontys.s3.rentride_be.domain.booking.CreateBookingRequest;
import nl.fontys.s3.rentride_be.domain.booking.CreateBookingResponse;
import nl.fontys.s3.rentride_be.domain.city.GetRouteResponse;
import nl.fontys.s3.rentride_be.persistance.BookingRepository;
import nl.fontys.s3.rentride_be.persistance.CarRepository;
import nl.fontys.s3.rentride_be.persistance.CityRepository;
import nl.fontys.s3.rentride_be.persistance.UserRepository;
import nl.fontys.s3.rentride_be.persistance.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    private GetRouteBetweenCitiesUseCase routeBetweenCitiesUseCase;

    @InjectMocks
    private CreateBookingUseCaseImpl createBookingUseCase;

    private CreateBookingRequest request;
    private UserEntity mockUser;
    private CarEntity mockCar;
    private CityEntity mockFromCity;
    private CityEntity mockToCity;
    private GetRouteResponse mockRouteResponse;
    private BookingEntity mockBookingEntity;

    @BeforeEach
    void setUp() {
        request = new CreateBookingRequest();
        request.setUserId(1L);
        request.setCarId(2L);
        request.setFromCityId(3L);
        request.setToCityId(4L);
        request.setStartDateTime(LocalDateTime.of(2024, 11, 15, 10, 0));
        request.setEndDateTime(LocalDateTime.of(2024, 11, 15, 14, 0));
        request.setCoverage(0);
        request.setTotalPrice(100.0);

        mockUser = new UserEntity();
        mockCar = new CarEntity();
        mockFromCity = CityEntity.builder().id(3L).build();
        mockToCity = CityEntity.builder().id(4L).build();

        mockRouteResponse = new GetRouteResponse();
        mockRouteResponse.setDistance("100.0");
        mockRouteResponse.setTime("60");
        mockRouteResponse.setImgUrl("http://example.com/map");

        mockBookingEntity = BookingEntity.builder().id(1L).build();
    }

    @Test
    void createBooking_ShouldReturnResponse_WhenAllEntitiesExist() {
        when(userRepository.findById(request.getUserId())).thenReturn(Optional.of(mockUser));
        when(carRepository.findById(request.getCarId())).thenReturn(Optional.of(mockCar));
        when(cityRepository.findById(request.getFromCityId())).thenReturn(Optional.of(mockFromCity));
        when(cityRepository.findById(request.getToCityId())).thenReturn(Optional.of(mockToCity));
        when(routeBetweenCitiesUseCase.getRoute(request.getFromCityId(), request.getToCityId())).thenReturn(mockRouteResponse);
        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(mockBookingEntity);

        CreateBookingResponse response = createBookingUseCase.createBooking(request);

        assertNotNull(response);
        assertEquals(1L, response.getBookingId());
    }

    @Test
    void createBooking_ShouldThrowNotFoundException_WhenUserNotFound() {
        when(userRepository.findById(request.getUserId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> createBookingUseCase.createBooking(request));
    }

    @Test
    void createBooking_ShouldThrowNotFoundException_WhenCarNotFound() {
        when(userRepository.findById(request.getUserId())).thenReturn(Optional.of(mockUser));
        when(carRepository.findById(request.getCarId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> createBookingUseCase.createBooking(request));
    }

    @Test
    void createBooking_ShouldThrowNotFoundException_WhenFromCityNotFound() {
        when(userRepository.findById(request.getUserId())).thenReturn(Optional.of(mockUser));
        when(carRepository.findById(request.getCarId())).thenReturn(Optional.of(mockCar));
        when(cityRepository.findById(request.getFromCityId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> createBookingUseCase.createBooking(request));
    }

    @Test
    void createBooking_ShouldThrowNotFoundException_WhenToCityNotFound() {
        when(userRepository.findById(request.getUserId())).thenReturn(Optional.of(mockUser));
        when(carRepository.findById(request.getCarId())).thenReturn(Optional.of(mockCar));
        when(cityRepository.findById(request.getFromCityId())).thenReturn(Optional.of(mockFromCity));
        when(cityRepository.findById(request.getToCityId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> createBookingUseCase.createBooking(request));
    }

    @Test
    void createBooking_ShouldCalculateDistanceCorrectly() {
        when(userRepository.findById(request.getUserId())).thenReturn(Optional.of(mockUser));
        when(carRepository.findById(request.getCarId())).thenReturn(Optional.of(mockCar));
        when(cityRepository.findById(request.getFromCityId())).thenReturn(Optional.of(mockFromCity));
        when(cityRepository.findById(request.getToCityId())).thenReturn(Optional.of(mockToCity));
        when(routeBetweenCitiesUseCase.getRoute(request.getFromCityId(), request.getToCityId())).thenReturn(mockRouteResponse);
        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(mockBookingEntity);

        createBookingUseCase.createBooking(request);

        verify(routeBetweenCitiesUseCase, times(1)).getRoute(request.getFromCityId(), request.getToCityId());
    }

    @Test
    void createBooking_ShouldSaveBookingWithCorrectValues() {
        when(userRepository.findById(request.getUserId())).thenReturn(Optional.of(mockUser));
        when(carRepository.findById(request.getCarId())).thenReturn(Optional.of(mockCar));
        when(cityRepository.findById(request.getFromCityId())).thenReturn(Optional.of(mockFromCity));
        when(cityRepository.findById(request.getToCityId())).thenReturn(Optional.of(mockToCity));
        when(routeBetweenCitiesUseCase.getRoute(request.getFromCityId(), request.getToCityId())).thenReturn(mockRouteResponse);
        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(mockBookingEntity);

        createBookingUseCase.createBooking(request);

        verify(bookingRepository).save(argThat(booking ->
                booking.getUser().equals(mockUser) &&
                        booking.getCar().equals(mockCar) &&
                        booking.getStartCity().equals(mockFromCity) &&
                        booking.getEndCity().equals(mockToCity) &&
                        booking.getDistance() == 100.0 &&
                        booking.getTotalPrice() == request.getTotalPrice() &&
                        booking.getStatus() == BookingStatus.Unpaid
        ));
    }
}