import * as jwtDecode from "jwt-decode";
import { post } from "../baseService";

const TokenManager = {
  getAccessToken: () => sessionStorage.getItem("accessToken"),
  getClaims: () => {
    const claims = sessionStorage.getItem("claims");
    if (!claims) {
      return undefined;
    }
    return JSON.parse(claims);
  },
  setAccessToken: (token) => {
    if (!token) return;

    const claims = jwtDecode.jwtDecode(token);

    sessionStorage.setItem("accessToken", token);
    sessionStorage.setItem("claims", JSON.stringify(claims));
    return claims;
  },
  renewToken: async () => {
    try {
      const response = await post(
        "auth/renew",
        {
          accessToken: TokenManager.getAccessToken(),
        },
        true
      );

      TokenManager.setAccessToken(response.accessToken);

      return response.accessToken;
    } catch (error) {
      console.error("Token renewal failed", error);
      TokenManager.clear();
      window.location.href = "/login";
      return null;
    }
  },
  initialize: () => {
    const accessToken = TokenManager.getAccessToken();
    if (accessToken) {
      const claims = jwtDecode.jwtDecode(accessToken);

      if (Date.now() < claims.exp * 1000) {
        TokenManager.setAccessToken(accessToken);
      } else {
        TokenManager.clear();
        window.location.href = "/login";
      }
    }
  },
  clear: () => {
    sessionStorage.removeItem("accessToken");
    sessionStorage.removeItem("claims");
  },

  isTokenExpiringSoon: (minutes) => {
    const accessToken = TokenManager.getAccessToken();
    if (!accessToken) return true;

    const claims = jwtDecode.jwtDecode(accessToken);
    const expirationTime = claims.exp * 1000;
    const currentTime = Date.now();
    const bufferTime = minutes * 60_000;

    return expirationTime - currentTime <= bufferTime;
  },
};

export default TokenManager;
