import * as jwtDecode from "jwt-decode";
import { get, post, put } from "./baseService";

export const getCurrentUserProfile = async () => {
    const responseData = await get(`users/currentUserProfile`);
    return responseData;
}

export const updateUserEmailSettings = async (obj) => {
    const responseData = await post(`users/updateUserEmailSettings`, obj);
    return responseData;
}

export const updateUserData = async (userId, obj) => {
    const responseData = await put(`users/${userId}`, obj);
    return responseData;
}

export const updateUserGoogleOAuthData = async (userId, newToken) => {
    const responseData = await put(`users/gauth/${userId}`, { oAuthId: newToken });

    return responseData;
}

