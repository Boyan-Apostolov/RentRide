package nl.fontys.s3.rentride_be.business.useCases.booking;

import java.time.LocalDateTime;
import java.util.Date;

public interface ScheduleBookingJobsUseCase {
    void scheduleStartAndEndJobs(Long bookingId, LocalDateTime startDate, LocalDateTime endDate);
}
