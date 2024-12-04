package nl.fontys.s3.rentride_be.business.impl.auction;

import com.stripe.exception.StripeException;
import lombok.AllArgsConstructor;
import nl.fontys.s3.rentride_be.business.exception.InvalidOperationException;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
import nl.fontys.s3.rentride_be.business.use_cases.auction.AuctionEndProcessUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.auth.EmailerUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.payment.CreatePaymentSessionUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.payment.CreatePaymentUseCase;
import nl.fontys.s3.rentride_be.business.use_cases.payment.UpdatePaymentUseCase;
import nl.fontys.s3.rentride_be.domain.payment.CreatePaymentRequest;
import nl.fontys.s3.rentride_be.domain.user.EmailType;
import nl.fontys.s3.rentride_be.persistance.AuctionRepository;
import nl.fontys.s3.rentride_be.persistance.entity.AuctionEntity;
import nl.fontys.s3.rentride_be.persistance.entity.BidEntity;
import nl.fontys.s3.rentride_be.persistance.entity.PaymentEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuctionEndProcessUseCaseImpl implements AuctionEndProcessUseCase {
    private AuctionRepository auctionRepository;
    private CreatePaymentUseCase createPaymentUseCase;
    private CreatePaymentSessionUseCase createPaymentSession;
    private UpdatePaymentUseCase updatePaymentUseCase;
    private EmailerUseCase emailerUseCase;

    @Override
    public void triggerPostAuctionProcesses(Long auctionId) throws StripeException {
        Optional<AuctionEntity> optionalAuction = auctionRepository.findById(auctionId);
        if (optionalAuction.isEmpty()) throw new NotFoundException("AuctionEnd->Auction");
        AuctionEntity auction = optionalAuction.get();

        if (auction.getEndDateTime().isAfter(LocalDateTime.now()))
            throw new InvalidOperationException("Auction not finished yet!");

        BidEntity highestBid = auction
                .getBids()
                .get(auction.getBids().size() - 1);

        if (highestBid == null) {
            //Auction has no bids
            auction.setCanBeClaimed(0);
            return;
        }

        auction.setWinnerUser(highestBid.getUser());

        auctionRepository.save(auction);

        createPaymentRequest(auctionId, highestBid);

        emailerUseCase.send(highestBid.getUser().getEmail(),
                "Auction won!",
                String.format("Contratulations! You won auction #%s. Please pay the bid amount in your Profile page, section payments to receive the car.", auction.getId()),
                EmailType.BOOKING);
    }

    private void createPaymentRequest(Long auctionId, BidEntity highestBid) throws StripeException {
        CreatePaymentRequest paymentRequest = CreatePaymentRequest.builder()
                .description(String.format("Won auction #%s", auctionId))
                .totalCost(highestBid.getAmount())
                .userId(highestBid.getUser().getId())
                .build();

        PaymentEntity paymentEntity = createPaymentUseCase.createPayment(paymentRequest);

        String paymentUrl = createPaymentSession.createPaymentSession(paymentRequest.getDescription(), paymentRequest.getTotalCost(), "auction", auctionId);

        paymentEntity.setStripeLink(paymentUrl);
        updatePaymentUseCase.updatePayment(paymentEntity);
    }
}
