package nl.fontys.s3.rentride_be.business.impl.deployment;

import lombok.RequiredArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.deployment.RenderManagementUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class RenderManagementUseCaseImpl implements RenderManagementUseCase {

    @Value("${API_KEY_RENDER}")
    private String renderApiKey;

    private final RestTemplate restTemplate;

    private static final String RENDER_API_URL = "https://api.render.com/v1/services/";

    @Override
    public String startRenderService(String serviceId) {
        return callRenderApi(serviceId, "/resume", HttpMethod.POST);
    }

    @Override
    public String stopRenderService(String serviceId) {
        return callRenderApi(serviceId, "/suspend", HttpMethod.POST);
    }

    @Override
    public String redeployRenderService(String serviceId) {
        return callRenderApi(serviceId, "/deploys", HttpMethod.POST);
    }

    @Override
    public String getRenderServiceStatus(String serviceId) {
        return callRenderApi(serviceId, "", HttpMethod.GET);
    }

    private String callRenderApi(String serviceId, String endpoint, HttpMethod method) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + renderApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                RENDER_API_URL + serviceId + endpoint,
                method,
                entity,
                String.class
        );
        return response.getBody();
    }
}