package nl.fontys.s3.rentride_be.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.discount.CreateDiscountPlanPurchaseUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.discount.CreateDiscountPlanUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.discount.DeleteDiscountPlanUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.discount.GetAllDiscountPlansUseCase;
import nl.fontys.s3.rentride_be.domain.discount.CreateDiscountPaymentRequest;
import nl.fontys.s3.rentride_be.domain.discount.CreateDiscountPlanRequest;
import nl.fontys.s3.rentride_be.domain.discount.CreateDiscountPlanResponse;
import nl.fontys.s3.rentride_be.domain.discount.DiscountPlan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("discountPlans")
@AllArgsConstructor
public class DiscountPlansController {
    private GetAllDiscountPlansUseCase getAllDiscountPlansUseCase;
    private DeleteDiscountPlanUseCase deleteDiscountPlanUseCase;
    private CreateDiscountPlanUseCase createDiscountPlanUseCase;
    private CreateDiscountPlanPurchaseUseCase createDiscountPlanPurchaseUseCase;

    @GetMapping
    public ResponseEntity<List<DiscountPlan>> getAllDiscountPlans() {
        return ResponseEntity.ok(this.getAllDiscountPlansUseCase.getAllDiscountPlans());
    }

    @DeleteMapping("{id}")
    @RolesAllowed({"ADMIN"})

    public ResponseEntity<String> deleteDiscountPlan(@PathVariable("id") Long id) {
        deleteDiscountPlanUseCase.deleteDiscountPlan(id);
        return ResponseEntity.noContent().build();

    }

    @PostMapping()
    @RolesAllowed({"ADMIN"})

    public ResponseEntity<CreateDiscountPlanResponse> createDiscountPlan(@RequestBody @Valid CreateDiscountPlanRequest request){
        CreateDiscountPlanResponse response = createDiscountPlanUseCase.createDiscountPlan(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/purchase")
    public ResponseEntity<CreateDiscountPlanResponse> createDiscountPlanPurchase(@RequestBody @Valid CreateDiscountPaymentRequest request){
        createDiscountPlanPurchaseUseCase.createDiscountPlanPurchase(request);
        return ResponseEntity.ok().build();
    }
}
