package nl.fontys.s3.rentride_be.business.impl.payment;

import com.stripe.exception.AuthenticationException;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class CreatePaymentSessionUseCaseImplTest {

    @InjectMocks
    private CreatePaymentSessionUseCaseImpl createPaymentSessionUseCase;

    @Mock
    private Session mockSession;

    private static final String STRIPE_API_KEY = "test_api_key";
    private static final String FRONTEND_URL = "http://localhost:3000/";

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(createPaymentSessionUseCase, "stripeApiKey", STRIPE_API_KEY);
        ReflectionTestUtils.setField(createPaymentSessionUseCase, "frontendUrl", FRONTEND_URL);
    }

    @Test
     void testCreatePaymentSession_Success() throws StripeException {
        String description = "Test Product";
        Double price = 12.34;
        String paymentType = "testType";
        Long relatedEntityId = 123L;

        when(mockSession.getUrl()).thenReturn("http://mocked_url");

        mockStatic(Session.class);
        when(Session.create(any(SessionCreateParams.class))).thenReturn(mockSession);

        String resultUrl = createPaymentSessionUseCase.createPaymentSession(description, price, paymentType, relatedEntityId);

        assertEquals("http://mocked_url", resultUrl);
    }

    @Test
    void testCreatePaymentSession_InvalidParams() {
        String description = "Test Product";
        Double invalidPrice = -10.0; // Invalid price
        String paymentType = "testType";
        Long relatedEntityId = 123L;

        assertThrows(AuthenticationException.class, () -> {
            createPaymentSessionUseCase.createPaymentSession(description, invalidPrice, paymentType, relatedEntityId);
        });
    }
}