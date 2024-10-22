//package nl.fontys.s3.rentride_be.persistance.impl;
//
//import lombok.AllArgsConstructor;
//import nl.fontys.s3.rentride_be.persistance.BookingRepository;
//import nl.fontys.s3.rentride_be.persistance.entity.BookingEntity;
//import nl.fontys.s3.rentride_be.persistance.entity.BookingStatus;
//import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//@Repository
//@AllArgsConstructor
//public class FakeBookingRepository implements BookingRepository {
//    private static long NEXT_ID = 1;
//    private final List<BookingEntity> savedBookings = new ArrayList<>();
//
//    @Override
//    public List<BookingEntity> findAll() {
//        return Collections.unmodifiableList(this.savedBookings);
//    }
//
//    @Override
//    public List<BookingEntity> findByCarId(Long carId) {
//        return this.savedBookings
//                .stream()
//                .filter(bookingEntity -> bookingEntity.getCar().getId().equals(carId))
//                .toList();
//    }
//
//    @Override
//    public List<BookingEntity> findByUserId(Long userId) {
//        return this.savedBookings
//                .stream()
//                .filter(bookingEntity -> bookingEntity.getUser().getId().equals(userId))
//                .toList();
//    }
//
//    //Used for checking if the car is available for the period
//    @Override
//    public List<BookingEntity> findByCarIdAndDates(Long carId, LocalDateTime startDate, LocalDateTime endDate) {
//        return this.savedBookings
//                .stream()
//                .filter(bookingEntity -> bookingEntity.getCar().getId().equals(carId))
//                .filter(bookingEntity ->
//                        (bookingEntity.getStartDateTime().isAfter(startDate) || bookingEntity.getStartDateTime().isEqual(startDate))
//                                && (bookingEntity.getEndDateTime().isBefore(endDate) || bookingEntity.getEndDateTime().isEqual(endDate))
//                )
//                .filter(bookingEntity -> bookingEntity.getBookingStatus() != BookingStatus.Canceled && bookingEntity.getBookingStatus() != BookingStatus.Finished)
//                .toList();
//    }
//
//    @Override
//    public BookingEntity findById(long bookingId) {
//        return this.savedBookings
//                .stream()
//                .filter(bookingEntity -> bookingEntity.getId().equals(bookingId))
//                .findFirst()
//                .orElse(null);
//    }
//
//    @Override
//    public BookingEntity save(BookingEntity booking) {
//        if (booking.getId() == null) {
//            booking.setId(NEXT_ID);
//            NEXT_ID++;
//            this.savedBookings.add(booking);
//        }
//        return booking;
//    }
//
//    @Override
//    public int count() {
//        return this.savedBookings.size();
//    }
//}
