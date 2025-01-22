package nl.fontys.s3.rentride_be.persistance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "discount_plan_purchase")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountPlanPurchaseEntity {
    @EmbeddedId
    private DiscountPlanPurchaseKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private UserEntity user;

    @ManyToOne
    @MapsId("discountPlanId")
    @JoinColumn(name = "discount_plan_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private DiscountPlanEntity discountPlan;

    @NotNull
    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;

    @Column(name = "remaining_uses")
    @NotNull
    private Integer remainingUses;
}

