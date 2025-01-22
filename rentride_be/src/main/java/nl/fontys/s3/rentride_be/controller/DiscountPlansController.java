package nl.fontys.s3.rentride_be.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.discount.*;
import nl.fontys.s3.rentride_be.domain.discount.*;
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
    private IsDiscountPlanBoughtUseCase isDiscountPlanBoughtUseCase;

    @GetMapping
    public ResponseEntity<List<DiscountPlan>> getAllDiscountPlans() {
        return ResponseEntity.ok(this.getAllDiscountPlansUseCase.getAllDiscountPlans());
    }

    @GetMapping("/is-bought")
    public ResponseEntity<IsDiscountPlanBoughtResponse> isDiscountPlanBoughtBySessionUser(@RequestParam("id") Long id) {
        IsDiscountPlanBoughtResponse response = this.isDiscountPlanBoughtUseCase.isDiscountPlanBought(id);
        return ResponseEntity.ok(response);
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
