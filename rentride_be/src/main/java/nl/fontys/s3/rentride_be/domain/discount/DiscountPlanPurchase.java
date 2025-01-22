package nl.fontys.s3.rentride_be.domain.discount;

import lombok.*;
import nl.fontys.s3.rentride_be.domain.user.User;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscountPlanPurchase {
    private User user;

    private DiscountPlan discountPlan;

    private LocalDateTime purchaseDate;

    private Integer remainingUses;
}
