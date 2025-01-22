package nl.fontys.s3.rentride_be.persistance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "discount_plan")

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DiscountPlanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    @NotNull
    private String title;

    @Column(name = "description")
    @NotNull
    private String description;

    @Column(name = "remaining_uses")
    @NotNull
    private Integer remainingUses;

    @Column(name = "discount_value")
    @NotNull
    private Integer discountValue;

    @NotNull
    @Column(name = "price")
    private double price;

    @OneToMany(mappedBy = "discountPlan", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<DiscountPlanPurchaseEntity> purchases;
}
