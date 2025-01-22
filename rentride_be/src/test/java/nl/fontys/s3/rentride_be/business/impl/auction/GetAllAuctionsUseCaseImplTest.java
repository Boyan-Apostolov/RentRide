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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllAuctionsUseCaseImplTest {
    @Mock
    private AuctionRepository auctionRepository;

    @InjectMocks
    private GetAllAuctionsUseCaseImpl getAllAuctionsUseCase;

    @Test
    void getAllAuctions_shouldReturnAllAuctions() {
        AuctionEntity auctionEntity1 = AuctionEntity.builder().id(1L).bids(List.of()).description("Auction 1").build();
        AuctionEntity auctionEntity2 = AuctionEntity.builder().id(2L).bids(List.of()).description("Auction 2").build();

        when(auctionRepository.findAll()).thenReturn(List.of(auctionEntity1, auctionEntity2));

        List<Auction> result = getAllAuctionsUseCase.getAllAuctions();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Auction::getId).containsExactly(1L, 2L);
        assertThat(result).extracting(Auction::getDescription).containsExactly("Auction 1", "Auction 2");

        verify(auctionRepository).findAll();
    }

    @Test
    void getAllAuctions_shouldReturnEmptyList_whenNoAuctionsExist() {
        when(auctionRepository.findAll()).thenReturn(List.of());

        List<Auction> result = getAllAuctionsUseCase.getAllAuctions();

        assertThat(result).isEmpty();
        verify(auctionRepository).findAll();
    }
}