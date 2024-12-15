package nl.fontys.s3.rentride_be;

import lombok.RequiredArgsConstructor;
import nl.fontys.s3.rentride_be.business.impl.auth.EmailerUseCaseImpl;
import nl.fontys.s3.rentride_be.business.use_cases.auth.EmailerUseCase;
import nl.fontys.s3.rentride_be.domain.user.EmailType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class FrontendPinger {
    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @Value("${FRONTEND_URL}")
    private String frontendUrl;

    private final RestTemplate restTemplate;
    private final EmailerUseCase emailerUseCase;
    private static final Logger logger = LoggerFactory.getLogger(FrontendPinger.class);


    @Scheduled(cron = "0 */5 * * * *")  // Every 5 minutes
    private void pingFrontend(){
        if (!"production".equals(activeProfile)) {
            return;
        }

        try {
            logger.info("Pinging frontend...");

            ResponseEntity<String> response = restTemplate.getForEntity(frontendUrl + "/ping", String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException();
            }


            logger.info("Pinging frontend...SUCCESS");

        } catch (Exception e) {
            emailerUseCase.send("boian4934@gmail.com",
                    "Frontend if down!!",
                    "Spring boot application cannot access frontend at \" https://rentride-fe.onrender.com \"",
                    EmailType.SUPPORT);
        }
    }
}
