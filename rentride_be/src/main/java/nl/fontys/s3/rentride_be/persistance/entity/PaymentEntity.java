package nl.fontys.s3.rentride_be.persistance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    @NotNull
    private String description;

    @Column(name = "amount")
    @NotNull
    private double amount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private UserEntity user;

    @Column(name = "is_paid")
    @NotNull
    private boolean isPaid;

    @Column(name = "stripe_link")
    @NotNull
    private String stripeLink;

    @NotNull
    @Column(name = "created_on")
    private LocalDateTime createdOn;


}
