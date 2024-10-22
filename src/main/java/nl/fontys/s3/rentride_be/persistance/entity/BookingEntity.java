package nl.fontys.s3.rentride_be.persistance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "booking")

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "booking_status")
    @Enumerated(EnumType.ORDINAL)
    private BookingStatus bookingStatus;

    @NotNull
    @Column(name = "start_date_time")
    private LocalDateTime startDateTime;

    @NotNull
    @Column(name = "end_date_time")
    private LocalDateTime endDateTime;

    @ManyToOne
    @JoinColumn(name = "start_city_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private CityEntity startCity;

    @ManyToOne
    @JoinColumn(name = "end_city_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private CityEntity endCity;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private CarEntity car;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private UserEntity user;

    @NotNull
    @Column(name = "distance")
    private Long distance;

    @NotNull
    @Column(name = "total_price")
    private Long totalPrice;
}
