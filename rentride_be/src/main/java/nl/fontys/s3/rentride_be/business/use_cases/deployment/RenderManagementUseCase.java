package nl.fontys.s3.rentride_be.business.use_cases.deployment;

public interface RenderManagementUseCase {
    String startRenderService(String serviceId);
    String stopRenderService(String serviceId);
    String redeployRenderService(String serviceId);
    String getRenderServiceStatus(String serviceId);
}