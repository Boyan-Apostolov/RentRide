package nl.fontys.s3.rentride_be.business.use_cases.booking;

import java.time.LocalDateTime;

public interface ScheduleBookingJobsUseCase {
    void scheduleStartAndEndJobs(Long bookingId, LocalDateTime startDate, LocalDateTime endDate);
}
