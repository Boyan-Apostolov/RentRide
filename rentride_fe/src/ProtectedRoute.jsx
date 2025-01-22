import React from "react";
import { Navigate, Outlet } from "react-router-dom";
import { isUserLoggedIn, isCurrentUserInRole } from "./api/authService";
const ProtectedRoute = ({ requiredRole, isPublic, isRestrictedToGuests }) => {
  const isAuthenticated = isUserLoggedIn();
  const hasRequiredRole = requiredRole
    ? isCurrentUserInRole(requiredRole)
    : true;

  if (isPublic) {
    return <Outlet />;
  }

  if (
    (isRestrictedToGuests && isAuthenticated) ||
    (!isAuthenticated && requiredRole) ||
    (requiredRole && !hasRequiredRole)
  ) {
    return <Navigate to="/" />;
  }

  return <Outlet />;
};

export default ProtectedRoute;
