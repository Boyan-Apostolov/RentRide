package nl.fontys.s3.rentride_be.controller;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import nl.fontys.s3.rentride_be.business.useCases.city.GetCityUseCase;
import nl.fontys.s3.rentride_be.business.useCases.payment.CreatePaymentSessionUseCase;
import nl.fontys.s3.rentride_be.domain.city.City;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("payments")
@AllArgsConstructor
public class PaymentsController {
   private CreatePaymentSessionUseCase createPaymentSessionUseCase;
   private GetCityUseCase getCityUseCase;

    @GetMapping("/success")
    public ResponseEntity success(@RequestParam("session_id") String sessionId) {
        try {
            Session session = Session.retrieve(sessionId);
            String paymentStatus = session.getPaymentStatus();

            if (Objects.equals(paymentStatus, "paid")) {
                //TODO: activate booking
                return ResponseEntity.ok("Payment successful");
            } else {
                //TODO: cancel booking
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/cancel")
    public ResponseEntity cancel() {
        //TODO: cancel booking

        return ResponseEntity.ok("Payment cancelled");
    }

    @GetMapping("/pay")
    public ResponseEntity<String> createCheckoutSession(@RequestParam("fromCityId") Long fromCityId, @RequestParam("toCityId") Long toCityId, @RequestParam("price") Long price) {
        //Todo: get booking id and get price
        City fromCity = getCityUseCase.getCity(fromCityId);
        City toCity = getCityUseCase.getCity(toCityId);

        String paymentDescription = String.format("Booking - (%s) -> (%s)", fromCity.getName(), toCity.getName());
        try {
            String url = this.createPaymentSessionUseCase.createPaymentSession(paymentDescription, price);

            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}