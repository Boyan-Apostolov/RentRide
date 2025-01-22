import { isNumber } from "chart.js/helpers";
import { get, post } from "./baseService";
import TokenManager from "./utils/TokenManager.jsx";
import * as jwtDecode from "jwt-decode";

export const login = async (loginObj) => {
  const responseData = await post(`auth/login`, loginObj);

  TokenManager.setAccessToken(responseData.accessToken);

  return responseData;
};

export const loginWithGoogle = async (googleCridential) => {
  const claims = jwtDecode.jwtDecode(googleCridential);
  const googleOAuthId = claims?.sub;

  const responseData = await post(`auth/login/google`, { oAuthId: googleOAuthId });

  TokenManager.setAccessToken(responseData.accessToken);

  return responseData;
}

export const register = async (registerObj) => {
  const responseData = await post(`auth/register`, registerObj);

  TokenManager.setAccessToken(responseData.accessToken);

  return responseData
}

export const logout = () => {
  TokenManager.clear();
}

export const isCurrentUserInRole = (role) => {
  return isUserLoggedIn() && TokenManager.getClaims()?.roles.includes(role);
}

export const isUserLoggedIn = () => {
  return isNumber(getCurrentUserId());
}

export const getCurrentUserId = () => {
  return TokenManager.getClaims()?.userId;
}
