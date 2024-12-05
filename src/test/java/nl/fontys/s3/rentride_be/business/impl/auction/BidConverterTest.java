package nl.fontys.s3.rentride_be.business.impl.auction;

import nl.fontys.s3.rentride_be.domain.auction.Bid;
import nl.fontys.s3.rentride_be.persistance.entity.BidEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

 class BidConverterTest {
    @Test
    void shouldConvertAllCountryFieldsToDomain(){
        BidEntity bidEntity = BidEntity
                .builder()
                .id(1L)
                .auction(null)
                .user(null)
                .amount(1)
                .dateTime(LocalDateTime.of(10,10,10,10,10))
                .build();

        Bid actual = BidConverter.convert(bidEntity);

        Bid expected = Bid.builder()
                .id(1L)
                .auction(null)
                .user(null)
                .amount(1)
                .dateTime(LocalDateTime.of(10,10,10,10,10))
                .build();

        assertEquals(expected, actual);
    }

    @Test
    void converterWithNullShouldReturnNull(){
        assertNull(BidConverter.convert(null));
    }
}
