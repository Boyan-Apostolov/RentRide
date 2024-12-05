package nl.fontys.s3.rentride_be.business.use_cases.auction;

import java.time.LocalDateTime;

public interface ScheduleAuctionEndJobUseCase {
    void scheduleAuctionEndJob(Long auctionId, LocalDateTime endDate);
}
