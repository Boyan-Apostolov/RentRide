package nl.fontys.s3.rentride_be.controller;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("payments")
@RequiredArgsConstructor
public class PaymentsController {

    @Value("${API_KEY_STRIPE}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    @GetMapping("/success")
    public ModelAndView success(@RequestParam("session_id") String sessionId) {
        try {
            Session session = Session.retrieve(sessionId);
            String paymentStatus = session.getPaymentStatus();

            if ("paid".equals(paymentStatus)) {
                return new ModelAndView("redirect:/cars");
            } else {
                return new ModelAndView("redirect:/cars");
            }
        } catch (Exception e) {
            return new ModelAndView("redirect:/cars");
        }
    }

    @GetMapping("/cancel")
    public ModelAndView cancel() {
        // Todo: Delete booking
        return new ModelAndView("redirect:/cars");
    }

    @GetMapping("/create-checkout-session")
    public ResponseEntity<String> createCheckoutSession() {
        try {
            SessionCreateParams.LineItem.PriceData.ProductData productData =
                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName("Booking - {carNumber} - {city1} - {city2}") // Product name
                            .build();

            SessionCreateParams.LineItem.PriceData priceData =
                    SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("eur")
                            .setProductData(productData)
                            .setUnitAmount(500L) // Amount in cents
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

            return ResponseEntity.ok(session.getUrl());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}