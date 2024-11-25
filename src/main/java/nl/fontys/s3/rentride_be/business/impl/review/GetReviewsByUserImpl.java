package nl.fontys.s3.rentride_be.business.impl.review;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.review.GetReviewsByUser;
import nl.fontys.s3.rentride_be.configuration.security.token.AccessToken;
import nl.fontys.s3.rentride_be.domain.review.Review;
import nl.fontys.s3.rentride_be.persistance.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetReviewsByUserImpl implements GetReviewsByUser {
    private ReviewRepository reviewRepository;
    private AccessToken accessToken;
    @Override
    public List<Review> getReviewsByUser() {
        return reviewRepository
                .findAllByUser_Id(accessToken.getUserId())
                .stream().map(ReviewConverter::convert)
                .toList();
    }
}
