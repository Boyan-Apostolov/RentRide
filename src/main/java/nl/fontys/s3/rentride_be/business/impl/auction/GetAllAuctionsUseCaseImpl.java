package nl.fontys.s3.rentride_be.business.impl.auction;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.auction.GetAllAuctionsUseCase;
import nl.fontys.s3.rentride_be.domain.auction.Auction;
import nl.fontys.s3.rentride_be.persistance.AuctionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllAuctionsUseCaseImpl implements GetAllAuctionsUseCase {
    private final AuctionRepository auctionRepository;

    @Value("${DEFAULT_PAGE_SIZE}")
    private int DefaultPageSize;

    @Override
    public List<Auction> getAllAuctions() {
        return auctionRepository
                .findAll()
                .stream().map(AuctionConverter::convert)
                .toList();
    }

    @Override
    public List<Auction> getAllAuctions(int page) {
        Pageable pageable = PageRequest.of(page, DefaultPageSize);

        return auctionRepository
                .findAll(pageable)
                .stream().map(AuctionConverter::convert)
                .toList();
    }
    @Override
    public Long getCount() {
        return this.auctionRepository.count();
    }
}
