package nl.fontys.s3.rentride_be.business.impl.auction;

import nl.fontys.s3.rentride_be.domain.auction.Auction;
import nl.fontys.s3.rentride_be.persistance.entity.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

 class AuctionConverterTest {
    @Test
    void shouldConvertAllCountryFieldsToDomain(){
        AuctionEntity auctionEntity = AuctionEntity
                .builder()
                .id(1L)
                .bids(List.of())
                .description("")
                .minBidAmount(1)
                .car(null)
                .endDateTime(LocalDateTime.of(10,12,20,10,0))
                .canBeClaimed(0)
                .winnerUser(null)
                .build();

        Auction actual = AuctionConverter.convert(auctionEntity);

        Auction expected = Auction.builder()
                .id(1L)
                .bids(List.of())
                .description("")
                .minBidAmount(1)
                .car(null)
                .endDateTime(LocalDateTime.of(10,12,20,10,0))
                .canBeClaimed(0)
                .winnerUser(null)
                .build();


        assertEquals(expected, actual);
    }

    @Test
    void converterWithNullShouldReturnNull(){
        assertNull(AuctionConverter.convert(null));
    }
}
