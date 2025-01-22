import { get } from "./baseService"

export const getRenderServiceStatus = async (serviceType) => {
    const responseData = await get(`deployments/render/status?serviceType=${serviceType}`);

    return {
        status: responseData.suspended,
        lastUpdate: responseData.updatedAt
    };
}

export const turnOnRenderService = async (serviceType) => {
    await get(`deployments/render/on?serviceType=${serviceType}`);
}

export const turnOffRenderService = async (serviceType) => {
    await get(`deployments/render/off?serviceType=${serviceType}`);
}

export const redeployRenderService = async (serviceType) => {
    await get(`deployments/render/redeploy?serviceType=${serviceType}`);
}