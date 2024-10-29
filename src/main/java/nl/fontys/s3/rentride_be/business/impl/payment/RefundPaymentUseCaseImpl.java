package nl.fontys.s3.rentride_be.business.impl.payment;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Refund;
import com.stripe.param.RefundCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.payment.RefundPaymentUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefundPaymentUseCaseImpl implements RefundPaymentUseCase {
    @Value("${API_KEY_STRIPE}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    @Override
    public String refundPayment(String paymentId) {
        try {
            RefundCreateParams params = RefundCreateParams.builder()
                    .setPaymentIntent(paymentId)
                    .build();

            Refund refund = Refund.create(params);

            return refund.getId(); // Return the refund ID or other relevant info
        } catch (StripeException e) {
            throw new RuntimeException("Refund failed for payment ID: " + paymentId, e);
        }
    }
}