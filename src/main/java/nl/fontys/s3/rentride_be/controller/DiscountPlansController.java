package nl.fontys.s3.rentride_be.controller;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.discount.GetAllDiscountPlansUseCase;
import nl.fontys.s3.rentride_be.domain.discount.DiscountPlan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("discountPlans")
@AllArgsConstructor
public class DiscountPlansController {
    private GetAllDiscountPlansUseCase getAllDiscountPlansUseCase;


    @GetMapping
    public ResponseEntity<List<DiscountPlan>> getAllDiscountPlans() {
        return ResponseEntity.ok(this.getAllDiscountPlansUseCase.getAllDiscountPlans());
    }
}
