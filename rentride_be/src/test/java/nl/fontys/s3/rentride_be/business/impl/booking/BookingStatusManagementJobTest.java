package nl.fontys.s3.rentride_be.business.impl.booking;

import nl.fontys.s3.rentride_be.business.jobs.BookingStatusManagementJob;
import nl.fontys.s3.rentride_be.business.use_cases.booking.UpdateBookingStatusUseCase;
import nl.fontys.s3.rentride_be.persistance.entity.BookingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingStatusManagementJobTest {

    @Mock
    private UpdateBookingStatusUseCase updateBookingStatusUseCase;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @InjectMocks
    private BookingStatusManagementJob bookingStatusManagementJob;

    private JobDataMap jobDataMap;

    @Mock
    private JobDetail jobDetail;

    @BeforeEach
    void setUp() {
        jobDataMap = new JobDataMap();
        jobDataMap.put("bookingId", 1L);
        jobDataMap.put("status", "Active");

        when(jobExecutionContext.getJobDetail()).thenReturn(jobDetail);
        when(jobDetail.getJobDataMap()).thenReturn(jobDataMap);
    }

    @Test
    void execute_shouldUpdateBookingStatus_whenJobIsExecuted() throws JobExecutionException {
        bookingStatusManagementJob.execute(jobExecutionContext);

        verify(updateBookingStatusUseCase).updateBookingStatus(1L, BookingStatus.Active);
    }

    @Test
    void execute_shouldThrowException_whenInvalidStatusProvided() {
        jobDataMap.put("status", "INVALID_STATUS");

        assertThrows(IllegalArgumentException.class, () -> bookingStatusManagementJob.execute(jobExecutionContext));
    }
}