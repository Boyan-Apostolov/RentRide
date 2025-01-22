import React, { useEffect } from "react";
import { logout } from "../../api/authService";
import { useNavigate } from "react-router-dom";

export default function LogoutPage() {
  const navigate = useNavigate();

  async function handleLogout() {
    await logout();
    navigate("/");
  }

  useEffect(() => {
    handleLogout();
  }, [navigate]);

  return <div>Logging out...</div>;
}
