package nl.fontys.s3.rentride_be.persistance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "auction")

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuctionEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "min_bid_amount")
    private double minBidAmount;

    @NotNull
    @Column(name = "end_date_time")
    private LocalDateTime endDateTime;

    @NotNull
    @Column(name = "unlock_code")
    private String unlockCode;

    @NotNull
    @Column(name = "is_code_used")
    private boolean isCodeUsed;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private CarEntity car;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private UserEntity winnerUser;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "auction_bids_mapping",
            joinColumns = @JoinColumn(name = "auction_id"),
            inverseJoinColumns = @JoinColumn(name = "bid_id"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<BidEntity> bids;
}
