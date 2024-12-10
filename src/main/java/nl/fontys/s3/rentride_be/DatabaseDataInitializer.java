package nl.fontys.s3.rentride_be;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.booking.UpdateBookingStatusUseCase;
import nl.fontys.s3.rentride_be.domain.car.CarFeatureType;
import nl.fontys.s3.rentride_be.persistance.*;
import nl.fontys.s3.rentride_be.persistance.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DatabaseDataInitializer {
    private final CityRepository cityRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CarFeatureRepository carFeatureRepository;
    private final DamageRepository damageRepository;
    private final ReviewRepository reviewRepository;
    private final DiscountPlanRepository discountPlanRepository;
    private final DiscountPlanPurchaseRepository discountPlanPurchaseRepository;
    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;
    private final MessageRepositoy messageRepositoy;

    private final UpdateBookingStatusUseCase updateBookingStatusUseCase;

    private final PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(DatabaseDataInitializer.class);

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initializeDatabase() {
        if (!"test".equals(activeProfile)) {

            populateCarFeatures();
            populateCities();
            populateUsers();
            populateCars();
            populateDamages();

            populateBookings();
            populateReviews();

            populateDiscountPlans();
            populatePurchasedDiscountPlans();

            populateAuctionsAndBids();
            populateMessages();

            tryFixMissedBookings();
            tryFixEmptyDiscountPlanPurchases();
        }
    }

    private void populateMessages() {
        if (this.messageRepositoy.count() == 0) {
            messageRepositoy.save(MessageEntity.builder()
                            .message("Can i get a refund ?")
                            .answer("Please submit a new message and provide an OrderId and more details.")
                            .user(userRepository.findById(1L).orElse(null))
                    .build());
        }
    }

    private void populateAuctionsAndBids() {
        if (auctionRepository.count() == 0) {
            AuctionEntity auction = auctionRepository.save(
                    AuctionEntity.builder()
                            .description("First test auction")
                            .car(carRepository.findById(1L).orElse(null))
                            .endDateTime(LocalDateTime.now().plusHours(1))
                            .minBidAmount(25)
                            .build()
            );

            BidEntity bid = BidEntity.builder()
                    .amount(30)
                    .dateTime(LocalDateTime.now())
                    .auction(auction) // Associate the bid with the auction
                    .user(userRepository.findById(1L).orElse(null))
                    .build();

            auction.setBids(new ArrayList<>(List.of(bid)));

            bidRepository.save(bid);
            auctionRepository.save(auction);
        }
    }

    private void tryFixEmptyDiscountPlanPurchases() {
        List<DiscountPlanPurchaseEntity> emptyDiscountPlanPurchases = discountPlanPurchaseRepository.findAllByRemainingUses(0);
        discountPlanPurchaseRepository.deleteAll(emptyDiscountPlanPurchases);
    }

    private void populatePurchasedDiscountPlans() {
        if (discountPlanPurchaseRepository.count() == 0) {
            DiscountPlanEntity discountPlanEntity = discountPlanRepository.findById(1L).orElse(DiscountPlanEntity.builder().build());
            discountPlanPurchaseRepository.save(DiscountPlanPurchaseEntity.builder()
                    .discountPlan(discountPlanEntity)
                    .purchaseDate(LocalDateTime.now())
                    .remainingUses(discountPlanEntity.getRemainingUses())
                    .user(userRepository.findById(1L).orElse(null))
                    .id(DiscountPlanPurchaseKey.builder()
                            .discountPlanId(1L)
                            .userId(1L)
                            .build())
                    .build());
        }
    }

    private void populateDiscountPlans() {
        if (discountPlanRepository.count() == 0) {
            discountPlanRepository.save(DiscountPlanEntity.builder()
                    .title("Silver Plan")
                    .description("Enjoy 15% discount on 10 rides!")
                    .remainingUses(10)
                    .discountValue(15)
                    .price(80.00)
                    .build());

            discountPlanRepository.save(DiscountPlanEntity.builder()
                    .title("Gold Plan")
                    .description("Get 20% discount on 20 rides! Ideal for regular users.")
                    .remainingUses(20)
                    .discountValue(20)
                    .price(150.00)
                    .build());

            discountPlanRepository.save(DiscountPlanEntity.builder()
                    .title("Platinum Plan")
                    .description("Unlock a 25% discount on unlimited rides without date restrictions.")
                    .remainingUses(Integer.MAX_VALUE) // unlimited rides
                    .discountValue(25)
                    .price(2000.00)
                    .build());

            discountPlanRepository.save(DiscountPlanEntity.builder()
                    .title("8 Warrior")
                    .description("Special 30% discount on 8 rides, use anytime.")
                    .remainingUses(8)
                    .discountValue(30)
                    .price(70.00)
                    .build());
        }
    }

    private void populateReviews() {
        if (reviewRepository.count() == 0) {
            reviewRepository.save(ReviewEntity.builder()
                    .booking(bookingRepository.findById(1L).orElse(null))
                    .text("I liked the experience")
                    .carCondition(3)
                    .carSpeed(5)
                    .valueForMoney(4)
                    .createdOn(LocalDateTime.now())
                    .user(userRepository.findById(1L).orElse(null))
                    .build());
        }
    }

    private void populateBookings() {
        if (bookingRepository.count() == 0) {
            bookingRepository.save(BookingEntity.builder()
                    .status(BookingStatus.Rated)
                    .startCity(this.cityRepository.findById(1L).orElse(null))
                    .endCity(this.cityRepository.findById(2L).orElse(null))
                    .startDateTime(LocalDateTime.of(2024, 10, 1, 10, 1))
                    .endDateTime(LocalDateTime.of(2024, 10, 2, 10, 1))
                    .user(this.userRepository.findById(1L).orElse(null))
                    .car(this.carRepository.findById(1L).orElse(null))
                    .distance(50)
                    .totalPrice(15)
                    .paymentId("")
                    .coverage(BookingCoverage.Premium)
                    .build());

            bookingRepository.save(BookingEntity.builder()
                    .status(BookingStatus.Finished)
                    .startCity(this.cityRepository.findById(1L).orElse(null))
                    .endCity(this.cityRepository.findById(3L).orElse(null))
                    .startDateTime(LocalDateTime.of(2024, 10, 1, 10, 1))
                    .endDateTime(LocalDateTime.of(2024, 10, 3, 10, 1))
                    .user(this.userRepository.findById(1L).orElse(null))
                    .car(this.carRepository.findById(3L).orElse(null))
                    .distance(50)
                    .totalPrice(25)
                    .paymentId("")
                    .coverage(BookingCoverage.No)
                    .build());

            bookingRepository.save(BookingEntity.builder()
                    .status(BookingStatus.Finished)
                    .startCity(this.cityRepository.findById(1L).orElse(null))
                    .endCity(this.cityRepository.findById(2L).orElse(null))
                    .startDateTime(LocalDateTime.of(2024, 11, 1, 10, 1))
                    .endDateTime(LocalDateTime.of(2024, 11, 2, 10, 1))
                    .user(this.userRepository.findById(1L).orElse(null))
                    .car(this.carRepository.findById(1L).orElse(null))
                    .distance(50)
                    .totalPrice(15)
                    .paymentId("")
                    .coverage(BookingCoverage.Premium)
                    .build());

            bookingRepository.save(BookingEntity.builder()
                    .status(BookingStatus.Canceled)
                    .startCity(this.cityRepository.findById(2L).orElse(null))
                    .endCity(this.cityRepository.findById(3L).orElse(null))
                    .startDateTime(LocalDateTime.of(2024, 11, 1, 10, 1))
                    .endDateTime(LocalDateTime.of(2024, 11, 2, 10, 1))
                    .user(this.userRepository.findById(1L).orElse(null))
                    .car(this.carRepository.findById(2L).orElse(null))
                    .distance(100)
                    .totalPrice(40)
                    .paymentId("")
                    .coverage(BookingCoverage.Top)
                    .build());

            bookingRepository.save(BookingEntity.builder()
                    .status(BookingStatus.Paid)
                    .startCity(this.cityRepository.findById(2L).orElse(null))
                    .endCity(this.cityRepository.findById(3L).orElse(null))
                    .startDateTime(LocalDateTime.now().plusDays(2))
                    .endDateTime(LocalDateTime.now().plusDays(3))
                    .user(this.userRepository.findById(1L).orElse(null))
                    .car(this.carRepository.findById(3L).orElse(null))
                    .distance(90)
                    .totalPrice(45)
                    .paymentId("")
                    .coverage(BookingCoverage.No)
                    .build());

            bookingRepository.save(BookingEntity.builder()
                    .status(BookingStatus.Active)
                    .startCity(this.cityRepository.findById(3L).orElse(null))
                    .endCity(this.cityRepository.findById(4L).orElse(null))
                    .startDateTime(LocalDateTime.now().minusHours(2))
                    .endDateTime(LocalDateTime.now().plusDays(1))
                    .user(this.userRepository.findById(1L).orElse(null))
                    .car(this.carRepository.findById(2L).orElse(null))
                    .distance(54)
                    .totalPrice(11.12)
                    .paymentId("")
                    .coverage(BookingCoverage.Premium)
                    .build());
        }
    }

    private void populateDamages() {
        if (this.damageRepository.count() == 0) {
            this.damageRepository.save(DamageEntity.builder()
                    .name("Keys")
                    .cost(10)
                    .iconUrl("https://img.freepik.com/premium-vector/vector-design-lost-key-icon-style_1134108-11142.jpg")
                    .build());

            this.damageRepository.save(DamageEntity.builder()
                    .name("Lights")
                    .cost(10)
                    .iconUrl("https://img.freepik.com/premium-vector/cracked-bulb-icon-outline-cracked-bulb-vector-icon-color-flat-isolated_96318-116440.jpg")
                    .build());

            this.damageRepository.save(DamageEntity.builder()
                    .name("Cleanliness")
                    .cost(10)
                    .iconUrl("https://img.freepik.com/premium-vector/cleaning-icon_1301102-3298.jpg")
                    .build());

            this.damageRepository.save(DamageEntity.builder()
                    .name("Windows")
                    .cost(20)
                    .iconUrl("https://img.freepik.com/free-vector/round-window-with-broken-glass_1308-73918.jpg?t=st=1730275178~exp=1730278778~hmac=8d6b5209cebad6f959c982a91d9723111a5ae2be1a97fe4bec50e5da07b70511")
                    .build());

            this.damageRepository.save(DamageEntity.builder()
                    .name("Tires")
                    .cost(30)
                    .iconUrl("https://img.freepik.com/free-vector/tire-stack-cartoon-vector-icon-illustration-transportation-object-icon-isolated-flat-vector_138676-11542.jpg?t=st=1730275150~exp=1730278750~hmac=cf409131d28f37c7d4a3240f285d5b00894a852997ed3b91f22f8b6a8638a9c3")
                    .build());


            this.damageRepository.save(DamageEntity.builder()
                    .name("Dents")
                    .cost(50)
                    .iconUrl("https://img.freepik.com/premium-vector/body-repair-color-icon_781202-1482.jpg")
                    .build());
        }
    }

    public void tryFixMissedBookings() {
        LocalDateTime now = LocalDateTime.now();

        // Find paid bookings with passed start time that should be ACTIVE
        List<BookingEntity> paidBookings = bookingRepository.findByStartDateTimeBeforeAndStatus(now, BookingStatus.Paid);
        for (BookingEntity booking : paidBookings) {
            updateBookingStatusUseCase.updateBookingStatus(booking.getId(), BookingStatus.Active);
            logger.info("Paid booking activated: Booking ID {}", booking.getId());
        }

        // Find missed bookings (bookings that should be marked as FINISHED)
        List<BookingEntity> missedBookings = bookingRepository.findByEndDateTimeBeforeAndStatus(now, BookingStatus.Active);
        for (BookingEntity booking : missedBookings) {
            updateBookingStatusUseCase.updateBookingStatus(booking.getId(), BookingStatus.Finished);
            logger.info("Missed booking fixed: Booking ID {}", booking.getId());
        }
    }

    private void populateCarFeatures() {
        if (carFeatureRepository.count() == 0) {
            this.carFeatureRepository.save(CarFeatureEntity.builder()
                    .featureType(CarFeatureType.Seats)
                    .featureText("5")
                    .build());

            this.carFeatureRepository.save(CarFeatureEntity.builder()
                    .featureType(CarFeatureType.Doors)
                    .featureText("4")
                    .build());

            this.carFeatureRepository.save(CarFeatureEntity.builder()
                    .featureType(CarFeatureType.Transmission)
                    .featureText("Auto")
                    .build());

            this.carFeatureRepository.save(CarFeatureEntity.builder()
                    .featureType(CarFeatureType.Transmission)
                    .featureText("Manual")
                    .build());

            this.carFeatureRepository.save(CarFeatureEntity.builder()
                    .featureType(CarFeatureType.Bonus)
                    .featureText("AC")
                    .build());
        }
    }

    private void populateCities() {
        if (this.cityRepository.count() == 0) {
            String street = "Some street";
            this.cityRepository.save(CityEntity.builder().name("Eindhoven").lat(51.4231).lon(5.4623).depoAdress(street).build());
            this.cityRepository.save(CityEntity.builder().name("Amsterdam").lat(52.3676).lon(4.9041).depoAdress(street).build());
            this.cityRepository.save(CityEntity.builder().name("Breda").lat(51.5719).lon(4.7683).depoAdress(street).build());
            this.cityRepository.save(CityEntity.builder().name("Utrecht").lat(52.0907).lon(5.1214).depoAdress(street).build());
        }
    }

    private void populateCars() {
        String featureNotFoundText = "Feature not found";
        if (this.carRepository.count() == 0) {
            CarEntity car1 = CarEntity.builder()
                    .make("Ford")
                    .model("Fiesta")
                    .registrationNumber("BT2142KX")
                    .fuelConsumption(6.5)
                    .city(this.cityRepository.findById(1L).orElse(null)) // Assuming Eindhoven
                    .photosBase64(List.of("https://i.ibb.co/WP7L59F/IMG-20220401-152405.jpg"))
                    .build();

            List<CarFeatureEntity> features1 = List.of(
                    carFeatureRepository.findById(1L).orElseThrow(() -> new RuntimeException(featureNotFoundText)),
                    carFeatureRepository.findById(2L).orElseThrow(() -> new RuntimeException(featureNotFoundText)),
                    carFeatureRepository.findById(3L).orElseThrow(() -> new RuntimeException(featureNotFoundText)),
                    carFeatureRepository.findById(5L).orElseThrow(() -> new RuntimeException(featureNotFoundText))
            );
            car1.setFeatures(features1);

            CarEntity car2 = CarEntity.builder()
                    .make("Audi")
                    .model("RS5")
                    .registrationNumber("AB1234CD")
                    .fuelConsumption(12.4)
                    .city(this.cityRepository.findById(2L).orElse(null)) // Assuming Amsterdam
                    .photosBase64(List.of("https://i.ibb.co/5ccy8Qt/H4s-IAAAAAAAAAFvzlo-G1t-Ii-BOTrayfuvp-Gh6-m1z-Jga-Gig-IGBg-ZGo-Dh-Tt-NOaz-I-2-Dh-CHs-CEtz-Ew-F-Sl-Mw.png"))
                    .build();

            List<CarFeatureEntity> features2 = List.of(
                    carFeatureRepository.findById(1L).orElseThrow(() -> new RuntimeException(featureNotFoundText)), // Seats
                    carFeatureRepository.findById(2L).orElseThrow(() -> new RuntimeException(featureNotFoundText)), // Doors
                    carFeatureRepository.findById(4L).orElseThrow(() -> new RuntimeException(featureNotFoundText)), // Transmission - Manual
                    carFeatureRepository.findById(5L).orElseThrow(() -> new RuntimeException(featureNotFoundText))  // AC
            );
            car2.setFeatures(features2);

            CarEntity car4 = CarEntity.builder()
                    .make("BMW")
                    .model("M5")
                    .registrationNumber("GH9012IJ")
                    .fuelConsumption(17.0)
                    .city(this.cityRepository.findById(4L).orElse(null)) // Assuming Utrecht
                    .photosBase64(List.of("https://i.ibb.co/fr5q7JP/bmw-m-series-seo-overview-ms-04.jpg"))
                    .build();

            car4.setFeatures(features2);

            this.carRepository.saveAll(List.of(car1, car2, car4));
        }
    }

    private void populateUsers() {
        if (this.userRepository.count() == 0) {
            UserEntity adminUser = UserEntity
                    .builder()
                    .name("Boyan Apostolov")
                    .email("admin@admin.com")
                    .password(passwordEncoder.encode("12345678"))
                    .birthDate(LocalDate.of(2004, 3, 12))
                    .bookingsEmails(true)
                    .damageEmails(true)
                    .promoEmails(false)
                    .build();
            UserRoleEntity adminRole = UserRoleEntity.builder().role(UserRole.ADMIN).user(adminUser).build();
            adminUser.setUserRoles(Set.of(adminRole));
            this.userRepository.save(adminUser);


            UserEntity customerUser = UserEntity
                    .builder()
                    .name("Average Customer")
                    .email("customer@customer.com")
                    .password(passwordEncoder.encode("12345678"))
                    .birthDate(LocalDate.of(2004, 3, 12))
                    .build();
            UserRoleEntity customerRole = UserRoleEntity.builder().role(UserRole.CUSTOMER).user(customerUser).build();
            customerUser.setUserRoles(Set.of(customerRole));
            this.userRepository.save(customerUser);
        }
    }
}
