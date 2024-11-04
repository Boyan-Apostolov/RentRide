package nl.fontys.s3.rentride_be.business.impl;

import nl.fontys.s3.rentride_be.DatabaseDataInitializer;
import nl.fontys.s3.rentride_be.business.use_cases.booking.UpdateBookingStatusUseCase;
import nl.fontys.s3.rentride_be.domain.car.CarFeatureType;
import nl.fontys.s3.rentride_be.persistance.*;
import nl.fontys.s3.rentride_be.persistance.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatabaseDataInitializerTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CarFeatureRepository carFeatureRepository;

    @Mock
    private DamageRepository damageRepository;

    @Mock
    private UpdateBookingStatusUseCase updateBookingStatusUseCase;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private DatabaseDataInitializer initializer;


    @BeforeEach
    void setup() {
        lenient().when(cityRepository.count()).thenReturn(0L);
        lenient().when(carFeatureRepository.count()).thenReturn(0L);
        lenient().when(userRepository.count()).thenReturn(0L);
        lenient().when(carRepository.count()).thenReturn(0L);
        lenient().when(damageRepository.count()).thenReturn(0L);
        lenient().when(reviewRepository.count()).thenReturn(0L);
        lenient().when(bookingRepository.count()).thenReturn(0L);

        CarFeatureEntity seats = CarFeatureEntity.builder().id(1L).featureType(CarFeatureType.Seats).featureText("5").build();
        CarFeatureEntity doors = CarFeatureEntity.builder().id(2L).featureType(CarFeatureType.Doors).featureText("4").build();
        CarFeatureEntity transmissionAutomatic = CarFeatureEntity.builder().id(3L).featureType(CarFeatureType.Transmission).featureText("Automatic").build();
        CarFeatureEntity transmissionManual = CarFeatureEntity.builder().id(4L).featureType(CarFeatureType.Transmission).featureText("Manual").build();
        CarFeatureEntity bonusAC = CarFeatureEntity.builder().id(5L).featureType(CarFeatureType.Bonus).featureText("AC").build();

        lenient().when(carFeatureRepository.findById(1L)).thenReturn(Optional.of(seats));
        lenient().when(carFeatureRepository.findById(2L)).thenReturn(Optional.of(doors));
        lenient().when(carFeatureRepository.findById(3L)).thenReturn(Optional.of(transmissionAutomatic));
        lenient().when(carFeatureRepository.findById(4L)).thenReturn(Optional.of(transmissionManual));
        lenient().when(carFeatureRepository.findById(5L)).thenReturn(Optional.of(bonusAC));

        CityEntity city1 = CityEntity.builder().id(1L).name("Eindhoven").build();
        CityEntity city2 = CityEntity.builder().id(2L).name("Amsterdam").build();
        UserEntity user = UserEntity.builder().id(1L).name("Boyan Apostolov").build();
        CarEntity car1 = CarEntity.builder().id(1L).make("Ford").build();
        BookingEntity booking = BookingEntity.builder().id(1L).build();

        lenient().when(cityRepository.findById(1L)).thenReturn(Optional.of(city1));
        lenient().when(cityRepository.findById(2L)).thenReturn(Optional.of(city2));
        lenient().when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        lenient().when(carRepository.findById(1L)).thenReturn(Optional.of(car1));
        lenient().when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
    }

    @Test
    void initializeDatabase_shouldPopulateDataIfEmpty() {
        initializer.initializeDatabase();

        verify(cityRepository, times(4)).save(any(CityEntity.class));
        verify(carFeatureRepository, times(5)).save(any(CarFeatureEntity.class));
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(carRepository, atLeastOnce()).saveAll(anyList());
        verify(damageRepository, times(6)).save(any(DamageEntity.class));
    }

    @Test
    void initializeDatabase_shouldNotPopulateDataIfNotEmpty() {
        when(cityRepository.count()).thenReturn(1L);
        when(carFeatureRepository.count()).thenReturn(1L);
        when(userRepository.count()).thenReturn(1L);
        when(carRepository.count()).thenReturn(1L);
        when(damageRepository.count()).thenReturn(1L);

        initializer.initializeDatabase();

        verify(cityRepository, never()).save(any(CityEntity.class));
        verify(carFeatureRepository, never()).save(any(CarFeatureEntity.class));
        verify(userRepository, never()).save(any(UserEntity.class));
        verify(carRepository, never()).saveAll(anyList());
        verify(damageRepository, never()).save(any(DamageEntity.class));
    }

    @Test
    void initializeDatabase_shouldCallTryFixMissedBookings() {
        when(bookingRepository.findPaidBookingsWithPassedStartTime(any())).thenReturn(List.of());
        when(bookingRepository.findMissedBookings(any())).thenReturn(List.of());

        initializer.initializeDatabase();

        verify(bookingRepository).findPaidBookingsWithPassedStartTime(any());
        verify(bookingRepository).findMissedBookings(any());
    }
}