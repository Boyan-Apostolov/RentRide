package nl.fontys.s3.rentride_be.business.use_cases.auction;

public interface UpdateAuctionCanBeClaimedUseCase {
    void updateState(Long auctionId, int canBeClaimed);
}
