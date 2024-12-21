package nl.fontys.s3.rentride_be.controller;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import nl.fontys.s3.rentride_be.business.use_cases.deployment.RenderManagementUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/deployments")
@RequiredArgsConstructor
@RolesAllowed({"ADMIN"})
public class DeploymentController {
    @Value("${FRONTEND_RENDER_ID}")
    private String frontendId;

    @Value("${BACKEND_RENDER_ID}")
    private String backendId;

    private final RenderManagementUseCase renderManagementUseCase;

    private String getServiceIdByType(String type){
        return Objects.equals(type, "frontend") ? frontendId : backendId;
    }

    @GetMapping("/render/status")
    public ResponseEntity<String> getRenderStatus(@RequestParam("serviceType") String serviceType) {
        String statusJson = renderManagementUseCase.getRenderServiceStatus(getServiceIdByType(serviceType));
        return ResponseEntity.ok(statusJson);
    }

    @GetMapping("/render/off")
    public ResponseEntity suspendRenderService(@RequestParam("serviceType") String serviceType) {
        renderManagementUseCase.stopRenderService(getServiceIdByType(serviceType));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/render/on")
    public ResponseEntity startRenderService(@RequestParam("serviceType") String serviceType) {
        renderManagementUseCase.startRenderService(getServiceIdByType(serviceType));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/render/redeploy")
    public ResponseEntity redeployRenderService(@RequestParam("serviceType") String serviceType) {
        renderManagementUseCase.redeployRenderService(getServiceIdByType(serviceType));
        return ResponseEntity.ok().build();
    }
}
