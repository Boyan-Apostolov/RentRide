package nl.fontys.s3.rentride_be.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.auction.*;
import nl.fontys.s3.rentride_be.domain.auction.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("auctions")
public class AuctionController {
    private final GetAllAuctionsUseCase getAllAuctionsUseCase;
    private final CreateAuctionUseCase createAuctionUseCase;
    private final GetAuctionUseCase getAuctionUseCase;
    private final PlaceBidUseCase placeBidUseCase;
    private final AuctionMessengerUseCase auctionMessengerUseCase;


    @MessageMapping("/auction/bid")
    @RolesAllowed({"ADMIN", "CUSTOMER"})

    public void handleBid(BidRequest bidRequest) {
        placeBidUseCase.placeBid(bidRequest); // Handles bid validation and broadcasting
    }

    @PostMapping("/notify")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<Void> sendNotificationToUsers(@RequestBody NotificationMessage message) {
        auctionMessengerUseCase.convertAndSend("/topic/publicmessages", message);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("{id}")
    @RolesAllowed({"ADMIN", "CUSTOMER"})

    public ResponseEntity<Auction> getAuction(@PathVariable(value = "id") final long id) {
        Auction auction = getAuctionUseCase.getAuction(id);

        if (auction == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(auction);
    }

    @GetMapping
    @RolesAllowed({"ADMIN", "CUSTOMER"})

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

    @PostMapping("/claim")
    @RolesAllowed({"ADMIN", "CUSTOMER"})
    public ResponseEntity<CreateAuctionResponse> claimAuction(@RequestBody @Valid CreateAuctionRequest request) {
        CreateAuctionResponse createAuctionResponse = createAuctionUseCase.createAuction(request);
        return ResponseEntity.ok(createAuctionResponse);
    }
}
