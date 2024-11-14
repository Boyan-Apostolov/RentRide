package nl.fontys.s3.rentride_be.business.impl.booking;

import nl.fontys.s3.rentride_be.business.impl.booking.ScheduleBookingJobsUseCaseImpl;
import nl.fontys.s3.rentride_be.business.jobs.BookingStatusManagementJob;
import nl.fontys.s3.rentride_be.persistance.entity.BookingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleBookingJobsUseCaseImplTest {

    @Mock
    private Scheduler scheduler;

    @InjectMocks
    private ScheduleBookingJobsUseCaseImpl scheduleBookingJobsUseCase;

    private Long bookingId;
    private Date triggerTime;
    private BookingStatus status;

    @BeforeEach
    void setUp() {
        bookingId = 1L;
        triggerTime = Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant());
        status = BookingStatus.Active;
    }

    @Test
    void scheduleBookingStatusJob_ShouldScheduleJobSuccessfully() throws SchedulerException {
        scheduleBookingJobsUseCase.scheduleBookingStatusJob(bookingId, triggerTime, status);

        JobDetail expectedJobDetail = JobBuilder.newJob(BookingStatusManagementJob.class)
                .withIdentity("bookingStatusJob_" + bookingId + "_" + status)
                .usingJobData("bookingId", bookingId)
                .usingJobData("status", status.name())
                .build();

        TriggerBuilder.newTrigger()
                .forJob(expectedJobDetail)
                .startAt(triggerTime)
                .build();

        verify(scheduler, times(1)).scheduleJob(any(JobDetail.class), any(Trigger.class));
    }

    @Test
    void scheduleStartAndEndJobs_ShouldScheduleStartAndEndJobsSuccessfully() throws SchedulerException {
        LocalDateTime startDate = LocalDateTime.now().plusHours(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);

        scheduleBookingJobsUseCase.scheduleStartAndEndJobs(bookingId, startDate, endDate);

        Date startDateParsed = Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
        Date endDateParsed = Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant());

        verify(scheduler, times(1)).scheduleJob(
                argThat(jobDetail -> jobDetail.getKey().getName().equals("bookingStatusJob_" + bookingId + "_Active")),
                argThat(trigger -> trigger.getStartTime().equals(startDateParsed))
        );

        verify(scheduler, times(1)).scheduleJob(
                argThat(jobDetail -> jobDetail.getKey().getName().equals("bookingStatusJob_" + bookingId + "_Finished")),
                argThat(trigger -> trigger.getStartTime().equals(endDateParsed))
        );
    }

    @Test
    void cancelJobsByBookingId_ShouldCancelMatchingJobs() throws SchedulerException {
        JobKey jobKey1 = new JobKey("bookingStatusJob_" + bookingId + "_Active", Scheduler.DEFAULT_GROUP);
        JobKey jobKey2 = new JobKey("bookingStatusJob_" + bookingId + "_Finished", Scheduler.DEFAULT_GROUP);
        Set<JobKey> jobKeys = Set.of(jobKey1, jobKey2);

        when(scheduler.getJobKeys(GroupMatcher.jobGroupEquals(Scheduler.DEFAULT_GROUP))).thenReturn(jobKeys);

        scheduleBookingJobsUseCase.cancelJobsByBookingId(bookingId);

        verify(scheduler, times(1)).deleteJob(jobKey1);
        verify(scheduler, times(1)).deleteJob(jobKey2);
    }

    @Test
    void cancelJobsByBookingId_ShouldHandleSchedulerExceptionGracefully() throws SchedulerException {
        JobKey jobKey1 = new JobKey("bookingStatusJob_" + bookingId + "_Active", Scheduler.DEFAULT_GROUP);
        Set<JobKey> jobKeys = Set.of(jobKey1);

        when(scheduler.getJobKeys(GroupMatcher.jobGroupEquals(Scheduler.DEFAULT_GROUP))).thenReturn(jobKeys);
        doThrow(new SchedulerException("Test exception")).when(scheduler).deleteJob(jobKey1);

        scheduleBookingJobsUseCase.cancelJobsByBookingId(bookingId);

        verify(scheduler, times(1)).deleteJob(jobKey1);
    }
}