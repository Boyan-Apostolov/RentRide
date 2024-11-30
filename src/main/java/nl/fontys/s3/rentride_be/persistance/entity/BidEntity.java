package nl.fontys.s3.rentride_be.persistance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bid")

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BidEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @NotNull
    @Column(name = "amount")
    private double amount;

    @ManyToOne
    @JoinColumn(name = "auction_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private AuctionEntity auction;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private UserEntity user;
}
