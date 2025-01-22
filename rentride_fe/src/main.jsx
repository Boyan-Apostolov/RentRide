import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import App from "./App.jsx";
import "./index.css";
import { GoogleOAuthProvider } from "@react-oauth/google";
import { CONSTANTS } from "./consts.js";

createRoot(document.getElementById("root")).render(
  <GoogleOAuthProvider clientId={CONSTANTS.GOOGLE_CLIENT_ID}>
    <App />
  </GoogleOAuthProvider>
);
