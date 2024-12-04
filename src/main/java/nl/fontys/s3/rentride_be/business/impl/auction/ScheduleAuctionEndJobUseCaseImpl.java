package nl.fontys.s3.rentride_be.business.impl.auction;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.jobs.BookingStatusManagementJob;
import nl.fontys.s3.rentride_be.business.use_cases.auction.ScheduleAuctionEndJobUseCase;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@AllArgsConstructor
public class ScheduleAuctionEndJobUseCaseImpl implements ScheduleAuctionEndJobUseCase {
    private Scheduler scheduler;
    private static final Logger logger = LoggerFactory.getLogger(ScheduleAuctionEndJobUseCaseImpl.class);

    public void scheduleAuctionEndJob(Long auctionId, Date triggerTime) {
        try {
            JobDetail jobDetail = JobBuilder.newJob(AuctionEndManagementJob.class)
                    .withIdentity("auctionEndJob_" + auctionId)
                    .usingJobData("auctionId", auctionId)
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .startAt(triggerTime)
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            logger.error("Error scheduling job: {}", e.getMessage());
        }
    }

    public void scheduleAuctionEndJob(Long auctionId, LocalDateTime endDate) {
        Date endDateParsed = Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant());
        scheduleAuctionEndJob(auctionId, endDateParsed);
    }
}
