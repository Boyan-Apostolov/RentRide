package nl.fontys.s3.rentride_be.controller;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.review.GetReviewsByCar;
import nl.fontys.s3.rentride_be.domain.city.GeoapifyResult;
import nl.fontys.s3.rentride_be.domain.review.Review;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("reviews")
@AllArgsConstructor
public class ReviewsController {
    private GetReviewsByCar getReviewsByCar;

    @GetMapping()
    public ResponseEntity<List<Review>> getReviews(@RequestParam(value = "carId") Long carId) {
       List<Review> reviews = this.getReviewsByCar.getReviewsByCar(carId);

        return ResponseEntity.ok().body(reviews);
    }
}
