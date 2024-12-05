package nl.fontys.s3.rentride_be.business.impl.auction;

import nl.fontys.s3.rentride_be.business.exception.InvalidOperationException;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.use_cases.auction.ScheduleAuctionEndJobUseCase;
import nl.fontys.s3.rentride_be.domain.auction.CreateAuctionRequest;
import nl.fontys.s3.rentride_be.domain.auction.CreateAuctionResponse;
import nl.fontys.s3.rentride_be.persistance.AuctionRepository;
import nl.fontys.s3.rentride_be.persistance.CarRepository;
import nl.fontys.s3.rentride_be.persistance.entity.AuctionEntity;
import nl.fontys.s3.rentride_be.persistance.entity.CarEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateAuctionUseCaseImplTest {
    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private ScheduleAuctionEndJobUseCase scheduleAuctionEndJobUseCase;

    @InjectMocks
    private CreateAuctionUseCaseImpl createAuctionUseCase;

    @Test
    void createAuction_shouldThrowInvalidOperationExceptionWhenEndDateIsBeforeNow() {
        CreateAuctionRequest request = CreateAuctionRequest.builder()
                .endDateTime(LocalDateTime.now().minusDays(1))
                .build();

        assertThatThrownBy(() -> createAuctionUseCase.createAuction(request))
                .isInstanceOf(InvalidOperationException.class)
                .hasMessage("400 BAD_REQUEST \"End date cannot be before start date\"");

        verifyNoInteractions(carRepository, auctionRepository, scheduleAuctionEndJobUseCase);
    }

    @Test
    void createAuction_shouldThrowNotFoundExceptionWhenCarDoesNotExist() {
        CreateAuctionRequest request = CreateAuctionRequest.builder()
                .car(1L)
                .endDateTime(LocalDateTime.now().plusDays(1))
                .build();

        when(carRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> createAuctionUseCase.createAuction(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("400 BAD_REQUEST \"CreateAuction->Car_NOT_FOUND\"");

        verify(carRepository).findById(1L);
        verifyNoInteractions(auctionRepository, scheduleAuctionEndJobUseCase);
    }

    @Test
    void createAuction_shouldThrowInvalidOperationExceptionWhenCarIsNotExclusive() {
        CarEntity car = CarEntity.builder().id(1L).isExclusive(false).build();
        CreateAuctionRequest request = CreateAuctionRequest.builder()
                .car(1L)
                .endDateTime(LocalDateTime.now().plusDays(1))
                .build();

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));

        assertThatThrownBy(() -> createAuctionUseCase.createAuction(request))
                .isInstanceOf(InvalidOperationException.class)
                .hasMessage("400 BAD_REQUEST \"Only exclusive cars can be used in auctions\"");

        verify(carRepository).findById(1L);
        verifyNoInteractions(auctionRepository, scheduleAuctionEndJobUseCase);
    }

    @Test
    void createAuction_shouldCreateAuctionWhenValidRequest() {
        CarEntity car = CarEntity.builder().id(1L).isExclusive(true).build();
        CreateAuctionRequest request = CreateAuctionRequest.builder()
                .car(1L)
                .endDateTime(LocalDateTime.now().plusDays(1))
                .minBidAmount(500.0)
                .description("Auction for exclusive car")
                .build();

        AuctionEntity savedAuction = AuctionEntity.builder()
                .id(1L)
                .car(car)
                .endDateTime(request.getEndDateTime())
                .minBidAmount(request.getMinBidAmount())
                .description(request.getDescription())
                .build();

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(auctionRepository.save(any(AuctionEntity.class))).thenReturn(savedAuction);

        CreateAuctionResponse response = createAuctionUseCase.createAuction(request);

        assertThat(response.getAuctionId()).isEqualTo(savedAuction.getId());

        verify(carRepository).findById(1L);
        verify(auctionRepository).save(any(AuctionEntity.class));
        verify(scheduleAuctionEndJobUseCase).scheduleAuctionEndJob(savedAuction.getId(), savedAuction.getEndDateTime());
    }
}