package nl.fontys.s3.rentride_be.business.impl.auction;

import com.stripe.exception.StripeException;
import nl.fontys.s3.rentride_be.business.exception.InvalidOperationException;
import nl.fontys.s3.rentride_be.business.exception.NotFoundException;
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
import nl.fontys.s3.rentride_be.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class AuctionEndProcessUseCaseImplTest {
    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private CreatePaymentUseCase createPaymentUseCase;

    @Mock
    private CreatePaymentSessionUseCase createPaymentSession;

    @Mock
    private UpdatePaymentUseCase updatePaymentUseCase;

    @Mock
    private EmailerUseCase emailerUseCase;

    @InjectMocks
    private AuctionEndProcessUseCaseImpl auctionEndProcessUseCase;

    @Test
    void triggerPostAuctionProcesses_shouldThrowNotFoundExceptionWhenAuctionDoesNotExist() {
        Long auctionId = 1L;
        when(auctionRepository.findById(auctionId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> auctionEndProcessUseCase.triggerPostAuctionProcesses(auctionId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("400 BAD_REQUEST \"AuctionEnd->Auction_NOT_FOUND\"");

        verify(auctionRepository).findById(auctionId);
        verifyNoInteractions(createPaymentUseCase, createPaymentSession, updatePaymentUseCase, emailerUseCase);
    }

    @Test
    void triggerPostAuctionProcesses_shouldThrowInvalidOperationExceptionWhenAuctionNotFinished() {
        Long auctionId = 1L;
        AuctionEntity auctionEntity = AuctionEntity.builder().id(auctionId).endDateTime(LocalDateTime.now().plusDays(1)).build();
        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auctionEntity));

        assertThatThrownBy(() -> auctionEndProcessUseCase.triggerPostAuctionProcesses(auctionId))
                .isInstanceOf(InvalidOperationException.class)
                .hasMessage("400 BAD_REQUEST \"Auction not finished yet!\"");

        verify(auctionRepository).findById(auctionId);
        verifyNoInteractions(createPaymentUseCase, createPaymentSession, updatePaymentUseCase, emailerUseCase);
    }

    @Test
    void triggerPostAuctionProcesses_shouldSetCanBeClaimedToZeroWhenNoBids() throws StripeException {
        Long auctionId = 1L;
        AuctionEntity auctionEntity = AuctionEntity.builder()
                .id(auctionId)
                .endDateTime(LocalDateTime.now().minusDays(1))
                .bids(Collections.emptyList())
                .build();
        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auctionEntity));

        auctionEndProcessUseCase.triggerPostAuctionProcesses(auctionId);

        verify(auctionRepository).findById(auctionId);
        verify(auctionRepository).save(auctionEntity);
        verifyNoInteractions(createPaymentUseCase, createPaymentSession, updatePaymentUseCase, emailerUseCase);
    }

    @Test
    void triggerPostAuctionProcesses_shouldSendEmailAndCreatePaymentForWinner() throws StripeException {
        Long auctionId = 1L;
        Long userId = 2L;
        UserEntity winnerUser = UserEntity.builder().id(userId).email("winner@example.com").build();
        BidEntity highestBid = BidEntity.builder().user(winnerUser).amount(500.0).build();

        AuctionEntity auctionEntity = AuctionEntity.builder()
                .id(auctionId)
                .endDateTime(LocalDateTime.now().minusDays(1))
                .bids(Collections.singletonList(highestBid))
                .build();

        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auctionEntity));

        CreatePaymentRequest paymentRequest = CreatePaymentRequest.builder()
                .description(String.format("Won auction #%s", auctionId))
                .totalCost(500.0)
                .userId(userId)
                .build();

        PaymentEntity paymentEntity = PaymentEntity.builder().id(1L).build();

        when(createPaymentUseCase.createPayment(paymentRequest)).thenReturn(paymentEntity);
        when(createPaymentSession.createPaymentSession(paymentRequest.getDescription(), paymentRequest.getTotalCost(), "auction", auctionId))
                .thenReturn("http://payment-link");

        auctionEndProcessUseCase.triggerPostAuctionProcesses(auctionId);

        verify(auctionRepository).findById(auctionId);
        verify(auctionRepository).save(auctionEntity);
        verify(createPaymentUseCase).createPayment(paymentRequest);
        verify(createPaymentSession).createPaymentSession(paymentRequest.getDescription(), paymentRequest.getTotalCost(), "auction", auctionId);
        verify(updatePaymentUseCase).updatePayment(paymentEntity);
        verify(emailerUseCase).send(
                eq("winner@example.com"),
                eq("Auction won!"),
                contains("You won auction #1"),
                eq(EmailType.BOOKING)
        );
    }
}