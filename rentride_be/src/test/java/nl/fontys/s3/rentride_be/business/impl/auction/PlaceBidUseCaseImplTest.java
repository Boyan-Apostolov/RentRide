package nl.fontys.s3.rentride_be.business.impl.auction;

import nl.fontys.s3.rentride_be.business.exception.InvalidOperationException;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.use_cases.auction.AuctionMessengerUseCase;
import nl.fontys.s3.rentride_be.domain.auction.BidRequest;
import nl.fontys.s3.rentride_be.domain.auction.BidResponse;
import nl.fontys.s3.rentride_be.persistance.AuctionRepository;
import nl.fontys.s3.rentride_be.persistance.UserRepository;
import nl.fontys.s3.rentride_be.persistance.entity.AuctionEntity;
import nl.fontys.s3.rentride_be.persistance.entity.BidEntity;
import nl.fontys.s3.rentride_be.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaceBidUseCaseImplTest {

    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuctionMessengerUseCase auctionMessengerUseCase;

    @InjectMocks
    private PlaceBidUseCaseImpl placeBidUseCase;

    @Test
    void placeBid_shouldPlaceNewBidWhenValid() {
        Long auctionId = 1L;
        Long userId = 2L;
        Double bidAmount = 1000.0;

        AuctionEntity auctionEntity = AuctionEntity.builder()
                .id(auctionId)
                .bids(new ArrayList<>())
                .build();

        UserEntity userEntity = UserEntity.builder()
                .id(userId)
                .name("Test User")
                .build();

        BidRequest bidRequest = BidRequest.builder()
                .auctionId(auctionId)
                .userId(userId)
                .bidAmount(bidAmount)
                .build();

        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auctionEntity));
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        BidResponse result = placeBidUseCase.placeBid(bidRequest);

        assertThat(result).isNotNull();
        assertThat(result.getAmount()).isEqualTo(bidAmount);
        assertThat(result.getUser().getId()).isEqualTo(userId);

        verify(auctionRepository).findById(auctionId);
        verify(userRepository).findById(userId);
        verify(auctionMessengerUseCase).broadcastNewBid(result);
    }

    @Test
    void placeBid_shouldThrowNotFoundExceptionWhenAuctionNotFound() {
        Long auctionId = 1L;
        BidRequest bidRequest = BidRequest.builder().auctionId(auctionId).build();

        when(auctionRepository.findById(auctionId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> placeBidUseCase.placeBid(bidRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("400 BAD_REQUEST \"Bid->Auction_NOT_FOUND\"");

        verify(auctionRepository).findById(auctionId);
        verifyNoInteractions(userRepository, auctionMessengerUseCase);
    }

    @Test
    void placeBid_shouldThrowNotFoundExceptionWhenUserNotFound() {
        Long auctionId = 1L;
        Long userId = 2L;

        AuctionEntity auctionEntity = AuctionEntity.builder()
                .id(auctionId)
                .bids(new ArrayList<>())
                .build();

        BidRequest bidRequest = BidRequest.builder()
                .auctionId(auctionId)
                .userId(userId)
                .build();

        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auctionEntity));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> placeBidUseCase.placeBid(bidRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("400 BAD_REQUEST \"Bid->User_NOT_FOUND\"");

        verify(auctionRepository).findById(auctionId);
        verify(userRepository).findById(userId);
        verifyNoInteractions(auctionMessengerUseCase);
    }

    @Test
    void placeBid_shouldThrowInvalidOperationExceptionWhenAuctionHasWinner() {
        Long auctionId = 1L;
        Long userId = 2L;

        AuctionEntity auctionEntity = AuctionEntity.builder()
                .id(auctionId)
                .winnerUser(UserEntity.builder().id(3L).build())
                .build();

        BidRequest bidRequest = BidRequest.builder()
                .auctionId(auctionId)
                .userId(userId)
                .build();

        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auctionEntity));

        assertThatThrownBy(() -> placeBidUseCase.placeBid(bidRequest))
                .isInstanceOf(InvalidOperationException.class)
                .hasMessage("400 BAD_REQUEST \"Auction already has winner\"");

        verify(auctionRepository).findById(auctionId);
        verifyNoInteractions(userRepository, auctionMessengerUseCase);
    }

    @Test
    void placeBid_shouldReturnNullWhenBidAmountNotHigherThanCurrentBid() {
        Long auctionId = 1L;
        Long userId = 2L;
        Double bidAmount = 1000.0;

        UserEntity previousUser = UserEntity.builder().id(3L).build();
        BidEntity currentHighestBid = BidEntity.builder().amount(1500.0).user(previousUser).build();

        AuctionEntity auctionEntity = AuctionEntity.builder()
                .id(auctionId)
                .bids(List.of(currentHighestBid))
                .build();

        UserEntity userEntity = UserEntity.builder().id(userId).build();

        BidRequest bidRequest = BidRequest.builder()
                .auctionId(auctionId)
                .userId(userId)
                .bidAmount(bidAmount)
                .build();

        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auctionEntity));
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        BidResponse result = placeBidUseCase.placeBid(bidRequest);

        assertThat(result).isNull();

        verify(auctionRepository).findById(auctionId);
        verify(userRepository).findById(userId);
        verifyNoInteractions(auctionMessengerUseCase);
    }
}