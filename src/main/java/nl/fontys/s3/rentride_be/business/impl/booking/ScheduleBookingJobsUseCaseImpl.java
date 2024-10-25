package nl.fontys.s3.rentride_be.business.impl.booking;

import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.jobs.BookingStatusManagementJob;
import nl.fontys.s3.rentride_be.business.use_cases.booking.ScheduleBookingJobsUseCase;
import nl.fontys.s3.rentride_be.persistance.entity.BookingStatus;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@AllArgsConstructor
public class ScheduleBookingJobsUseCaseImpl implements ScheduleBookingJobsUseCase {
    private Scheduler scheduler;
    private static final Logger logger = LoggerFactory.getLogger(ScheduleBookingJobsUseCaseImpl.class);

    public void scheduleBookingStatusJob(Long bookingId, Date triggerTime, BookingStatus status) {
        try {
            JobDetail jobDetail = JobBuilder.newJob(BookingStatusManagementJob.class)
                    .withIdentity("bookingStatusJob_" + bookingId + "_" + status)
                    .usingJobData("bookingId", bookingId)
                    .usingJobData("status", status.name())
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

    public void scheduleStartAndEndJobs(Long bookingId, LocalDateTime startDate, LocalDateTime endDate) {
        Date startDateParsed = Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
        scheduleBookingStatusJob(bookingId, startDateParsed, BookingStatus.Active);

        Date endDateParsed = Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant());
        scheduleBookingStatusJob(bookingId, endDateParsed, BookingStatus.Finished);
    }
}
