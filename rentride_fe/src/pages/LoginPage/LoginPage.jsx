import React, { useState } from "react";
import NavBar from "../../components/NavBar/NavBar";
import { login, loginWithGoogle } from "../../api/authService";
import { useNavigate } from "react-router-dom";
import { GoogleLogin } from "@react-oauth/google";
import LoadingSpinner from "../../components/LoadingSpinner/LoadingSpinner";
import { showError } from "../../api/utils/swalHelpers";

export default function LoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [validated, setValidated] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  const navigate = useNavigate();

  const handleGoogleLogin = async (credentialResponse) => {
    setIsLoading(true);
    const loginResult = await loginWithGoogle(credentialResponse.credential);
    setIsLoading(false);
    if (loginResult) {
      navigate("/");
    }
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setIsLoading(true);
    const form = event.currentTarget;
    if (form.checkValidity() === false) {
      event.stopPropagation();
    } else {
      const loginResult = await login({ email, password });
      setIsLoading(false);
      if (loginResult) {
        navigate("/");
      }
    }
    setValidated(true);
  };

  return (
    <>
      <NavBar bg="light" />
      <div className="container d-flex justify-content-center align-items-center vh-100">
        <div
          className="card p-4 shadow-lg"
          style={{ maxWidth: "400px", width: "100%" }}
        >
          <h2 className="text-center mb-4">Login</h2>
          <form
            noValidate
            onSubmit={handleSubmit}
            className={validated ? "was-validated" : ""}
          >
            <div className="mb-3">
              <label htmlFor="email" className="form-label">
                Email address
              </label>
              <input
                type="email"
                className="form-control"
                id="email"
                placeholder="Enter your email"
                required
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
              <div className="invalid-feedback">
                Please enter a valid email.
              </div>
            </div>
            <div className="mb-3">
              <label htmlFor="password" className="form-label">
                Password
              </label>
              <input
                type="password"
                className="form-control"
                id="password"
                placeholder="Enter your password"
                required
                minLength="6"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
              <div className="invalid-feedback">Password is required.</div>
            </div>
            {isLoading ? (
              <LoadingSpinner />
            ) : (
              <button type="submit" className="btn btn-primary w-100">
                Login
              </button>
            )}
            <hr></hr>
            {isLoading ? (
              <LoadingSpinner />
            ) : (
              <GoogleLogin
                onSuccess={handleGoogleLogin}
                onError={() => {
                  showError("Login with Google failed!");
                }}
              />
            )}
          </form>
        </div>
      </div>
    </>
  );
}
