package nl.fontys.s3.rentride_be.business.impl.payment;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import nl.fontys.s3.rentride_be.business.useCases.payment.CreatePaymentSessionUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatePaymentSessionUseCaseImpl implements CreatePaymentSessionUseCase {
    @Value("${API_KEY_STRIPE}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    @Override
    public String createPaymentSession(String description, Long price) throws StripeException {
        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(description) // Product name
                        .build();

        SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("eur")
                        .setProductData(productData)
                        .setUnitAmount(price * 100) // Amount in cents
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
                .setSuccessUrl("http://localhost:8080/payments/success?session_id={CHECKOUT_SESSION_ID}")  // Pass session_id
                .setCancelUrl("http://localhost:8080/payments/cancel")
                .build();

        Session session = Session.create(params);

        return session.getUrl();
    }
}
