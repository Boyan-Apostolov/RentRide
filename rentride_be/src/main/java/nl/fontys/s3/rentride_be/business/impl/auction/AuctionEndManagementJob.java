package nl.fontys.s3.rentride_be.business.impl.auction;

import com.stripe.exception.StripeException;
import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.InvalidOperationException;
import nl.fontys.s3.rentride_be.business.use_cases.auction.AuctionEndProcessUseCase;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuctionEndManagementJob implements Job {
    private AuctionEndProcessUseCase auctionEndProcessUseCase;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Long auctionId = context.getJobDetail().getJobDataMap()
                .getLong("auctionId");

        try {
            auctionEndProcessUseCase.triggerPostAuctionProcesses(auctionId);
        } catch (StripeException e) {
            throw new InvalidOperationException(e.getMessage());
        }
    }
}
