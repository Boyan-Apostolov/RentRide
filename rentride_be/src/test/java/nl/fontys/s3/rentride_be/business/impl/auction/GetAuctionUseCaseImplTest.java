package nl.fontys.s3.rentride_be.business.impl.auction;

import nl.fontys.s3.rentride_be.domain.auction.Auction;
import nl.fontys.s3.rentride_be.persistance.AuctionRepository;
import nl.fontys.s3.rentride_be.persistance.entity.AuctionEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAuctionUseCaseImplTest {

    @Mock
    private AuctionRepository auctionRepository;

    @InjectMocks
    private GetAuctionUseCaseImpl getAuctionUseCase;

    @Test
    void getAuction_shouldReturnAuction_whenAuctionExists() {
        AuctionEntity auctionEntity = AuctionEntity.builder().id(1L).bids(List.of()).description("Auction 1").build();

        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auctionEntity));

        Auction result = getAuctionUseCase.getAuction(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDescription()).isEqualTo("Auction 1");

        verify(auctionRepository).findById(1L);
    }

    @Test
    void getAuction_shouldReturnNull_whenAuctionDoesNotExist() {
        when(auctionRepository.findById(1L)).thenReturn(Optional.empty());

        Auction result = getAuctionUseCase.getAuction(1L);

        assertThat(result).isNull();
        verify(auctionRepository).findById(1L);
    }
}