package nl.fontys.s3.rentride_be.business.jobs;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.booking.UpdateBookingStatusUseCase;
import nl.fontys.s3.rentride_be.persistance.entity.BookingStatus;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BookingStatusManagementJob implements Job {
    private UpdateBookingStatusUseCase updateBookingStatusUseCase;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Long bookingId = context.getJobDetail().getJobDataMap()
                .getLong("bookingId");

        String statusString = context.getJobDetail().getJobDataMap()
                .getString("status");

        BookingStatus status = BookingStatus.valueOf(statusString);

        updateBookingStatusUseCase.updateBookingStatus(bookingId, status);
    }
}
