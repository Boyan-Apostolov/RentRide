package nl.fontys.s3.rentride_be.business.impl.payment;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.payment.CreatePaymentSessionUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatePaymentSessionUseCaseImpl implements CreatePaymentSessionUseCase {
    @Value("${API_KEY_STRIPE}")
    private String stripeApiKey;

    @Value("${FRONTEND_URL}")
    private String frontendUrl;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    @Override
    public String createPaymentSession(String description, Double price, String paymentType, Long relatedEntityId) throws StripeException {
        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(description) // Product name
                        .build();

        Long priceInCents = (long)(price * 100);
        SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("eur")
                        .setProductData(productData)
                        .setUnitAmount(priceInCents) // Amount in cents
                        .build();

        SessionCreateParams.LineItem lineItem =
                SessionCreateParams.LineItem.builder()
                        .setPriceData(priceData)
                        .setQuantity(1L)
                        .build();

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addLineItem(lineItem)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(frontendUrl + "pay-handle?paymentType=" + paymentType + "&responseType=success&sessionId={CHECKOUT_SESSION_ID}&entityId=" + relatedEntityId)
                .setCancelUrl(frontendUrl + "pay-handle?paymentType=" + paymentType + "&responseType=cancel&entityId=" + relatedEntityId)
                .build();

        Session session = Session.create(params);

        return session.getUrl();
    }
}
