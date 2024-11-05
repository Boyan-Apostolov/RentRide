package nl.fontys.s3.rentride_be.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.review.DeleteReviewUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.review.GetReviewsByCar;
import nl.fontys.s3.rentride_be.business.use_cases.review.UpdateReviewUseCase;
import nl.fontys.s3.rentride_be.domain.review.Review;
import nl.fontys.s3.rentride_be.domain.review.UpdateReviewRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("reviews")
@AllArgsConstructor
public class ReviewsController {
    private GetReviewsByCar getReviewsByCar;
    private DeleteReviewUseCase deleteReviewUseCase;
    private UpdateReviewUseCase updateReviewUseCase;

    @GetMapping()
    public ResponseEntity<List<Review>> getReviews(@RequestParam(value = "carId") Long carId) {
       List<Review> reviews = this.getReviewsByCar.getReviewsByCar(carId);

        return ResponseEntity.ok().body(reviews);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        deleteReviewUseCase.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateReview(@PathVariable("id") long id,
                                          @RequestBody @Valid UpdateReviewRequest request) {
        request.setId(id);
        updateReviewUseCase.updateReview(request);
        return ResponseEntity.noContent().build();
    }
}
