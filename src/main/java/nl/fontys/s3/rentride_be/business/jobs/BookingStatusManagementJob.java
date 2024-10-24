package nl.fontys.s3.rentride_be.business.jobs;

import nl.fontys.s3.rentride_be.business.useCases.booking.UpdateBookingStatusUseCase;
import nl.fontys.s3.rentride_be.persistance.entity.BookingStatus;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookingStatusManagementJob implements Job {
    @Autowired
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
