package nl.fontys.s3.rentride_be.business.use_cases.auction;

import com.stripe.exception.StripeException;

public interface AuctionEndProcessUseCase {
    void triggerPostAuctionProcesses(Long auctionId) throws StripeException;
}
