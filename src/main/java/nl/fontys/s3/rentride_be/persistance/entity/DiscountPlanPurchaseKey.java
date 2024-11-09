package nl.fontys.s3.rentride_be.persistance.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountPlanPurchaseKey implements Serializable {
    private Long userId;
    private Long discountPlanId;
}
