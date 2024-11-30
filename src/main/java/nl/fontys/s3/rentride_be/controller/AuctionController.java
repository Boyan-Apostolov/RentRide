package nl.fontys.s3.rentride_be.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.auction.CreateAuctionUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.auction.GetAllAuctionsUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.auction.GetAuctionUseCase;
import nl.fontys.s3.rentride_be.domain.auction.Auction;
import nl.fontys.s3.rentride_be.domain.auction.CreateAuctionRequest;
import nl.fontys.s3.rentride_be.domain.auction.CreateAuctionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("auctions")
public class AuctionController {
    private final GetAllAuctionsUseCase getAllAuctionsUseCase;
    private final CreateAuctionUseCase createAuctionUseCase;
    private final GetAuctionUseCase getAuctionUseCase;

//    private final AuctionService auctionService;
//
//    @MessageMapping("/auctions/bid")
//    public void handleBid(BidRequest bidRequest) {
//        auctionService.placeBid(bidRequest);
//    }
//
//    public List<Auction> getOngoingAuctions() {
//        return auctionService.getActiveAuctions();
//    }

    @GetMapping("{id}")
    public ResponseEntity<Auction> getAuction(@PathVariable(value = "id") final long id) {
        Auction auction = getAuctionUseCase.getAuction(id);

        if (auction == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(auction);
    }

    @GetMapping
    public ResponseEntity<List<Auction>> getAuctions() {
        List<Auction> auctions = getAllAuctionsUseCase.getAllAuctions();

        return ResponseEntity.ok(auctions);
    }

    @PostMapping
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<CreateAuctionResponse> createAuction(@RequestBody @Valid CreateAuctionRequest request) {
        CreateAuctionResponse createAuctionResponse = createAuctionUseCase.createAuction(request);
        return ResponseEntity.ok(createAuctionResponse);
    }
}
