package nl.fontys.s3.rentride_be.business.impl.auction;

import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.persistance.AuctionRepository;
import nl.fontys.s3.rentride_be.persistance.entity.AuctionEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateAuctionCanBeClaimedUseCaseImplTest {

    @Mock
    private AuctionRepository auctionRepository;

    @InjectMocks
    private UpdateAuctionCanBeClaimedUseCaseImpl updateAuctionCanBeClaimedUseCase;

    @Test
    void updateState_shouldUpdateCanBeClaimedStateWhenAuctionExists() {
        Long auctionId = 1L;
        int newCanBeClaimedState = 1;

        AuctionEntity auctionEntity = AuctionEntity.builder()
                .id(auctionId)
                .canBeClaimed(0)
                .build();

        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auctionEntity));

        updateAuctionCanBeClaimedUseCase.updateState(auctionId, newCanBeClaimedState);

        verify(auctionRepository).findById(auctionId);
        verify(auctionRepository).save(auctionEntity);
        assertThat(auctionEntity.getCanBeClaimed()).isEqualTo(newCanBeClaimedState);
    }

    @Test
    void updateState_shouldThrowNotFoundExceptionWhenAuctionDoesNotExist() {
        Long auctionId = 1L;
        int newCanBeClaimedState = 1;

        when(auctionRepository.findById(auctionId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updateAuctionCanBeClaimedUseCase.updateState(auctionId, newCanBeClaimedState))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("400 BAD_REQUEST \"UpdateAuctionClaimed->Auction_NOT_FOUND\"");

        verify(auctionRepository).findById(auctionId);
        verify(auctionRepository, never()).save(any());
    }
}