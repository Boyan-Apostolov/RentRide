package nl.fontys.s3.rentride_be.persistance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "review")
@Entity

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_on")
    @NotNull
    private LocalDateTime createdOn;

    @Column(name = "text")
    @NotNull
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private UserEntity user;

    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private BookingEntity booking;

    @Column(name = "value_for_money")
    @NotNull
    private Integer valueForMoney;

    @Column(name = "car_condition")
    @NotNull
    private Integer carCondition;

    @Column(name = "car_speed")
    @NotNull
    private Integer carSpeed;




}
