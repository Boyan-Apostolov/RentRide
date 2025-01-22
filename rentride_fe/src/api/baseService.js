import axios from "axios";
import { CONSTANTS } from "../consts";
import { showError } from "./utils/swalHelpers";
import TokenManager from "./utils/TokenManager.jsx";

const makeRequest = async (method, endpoint, data = null, shouldOverrideAccessTokenCheck = false) => {
    const requestEndpoint = `${CONSTANTS.BACKEND_URL}/${endpoint}`;
    let accessToken = TokenManager.getAccessToken();
    try {
        if (!shouldOverrideAccessTokenCheck) {
            const renewedToken = await tryRenewAccessToken(accessToken);
            accessToken = renewedToken || accessToken;
        }

        const response = await axios({
            method, url: requestEndpoint,
            data: data,
            headers: (accessToken ? { Authorization: `Bearer ${accessToken}` } : {}),
        });
        return response.data;
    } catch (error) {
        const errorMessage = error.response
            ? `${error.response.status} - ${error.response.data.errors ? error.response.data.errors[0].error : error.response.data}`
            : `${requestEndpoint} - Network error`;
        showError(errorMessage);
        return false;
    }
};

export const post = (endpoint, data, shouldOverrideAccessTokenCheck) => makeRequest("post", endpoint, data, shouldOverrideAccessTokenCheck);
export const put = (endpoint, data) => makeRequest("put", endpoint, data);
export const get = (endpoint) => makeRequest("get", endpoint);
export const del = (endpoint) => makeRequest("delete", endpoint);

const tryRenewAccessToken = async (accessToken) => {
    if (accessToken && TokenManager.isTokenExpiringSoon(CONSTANTS.AUTH_TOKEN_EXPIRY_MINS)) {
        console.log("Renewing token");

        return await TokenManager.renewToken();
    }
    return accessToken;
};
