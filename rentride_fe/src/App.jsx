import React, { useEffect } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import HomePage from "./pages/Home/HomePage";
import SearchPage from "./pages/Search/SearchPage";
import AllCars from "./pages/AllCars/AllCars";
import BookingPage from "./pages/BookingPage/BookingPage";
import ManageCarPage from "./pages/ManageCarPage/ManageCarPage";
import PaymentHandlerPage from "./pages/PaymentHandlerPage/PaymentHandlerPage";
import AllBookings from "./pages/AllBookings/AllBookings";
import DiscountPlansPage from "./pages/DiscountPlans/DiscountPlansPage";
import CreateDiscountPlanPage from "./pages/CreateDiscountPlan/CreateDiscountPlanPage";
import CarInfoPage from "./pages/CarInfoPage/CarInfoPage";
import StatisticsPage from "./pages/Statistics/StatisticsPage";
import LoginPage from "./pages/LoginPage/LoginPage";
import LogoutPage from "./pages/Logout/LogoutPage";
import RegisterPage from "./pages/Register/RegisterPage";
import MyPaymentsPage from "./pages/MyPayments/MyPaymentsPage";
import ProtectedRoute from "./ProtectedRoute";

import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.bundle.min";
import "./App.css";
import "react-toastify/dist/ReactToastify.css";

import NotFoundPage from "./pages/NotFound/NotFoundPage";
import ProfilePage from "./pages/ProfilePage/ProfilePage";
import TokenManager from "./api/utils/TokenManager";
import AllAuctionsPage from "./pages/AllAuctionsPage/AllAuctionsPage";
import CreateAuctionPage from "./pages/CreateAuction/CreateAuctionPage";
import AuctionPage from "./pages/Auction/AuctionPage";
import ClaimAuctionPage from "./pages/ClaimAuction/ClaimAuctionPage";
import AllMessagesPage from "./pages/AllMessages/AllMessagesPage";
import DeploymentsPage from "./pages/Deployments/DeploymentsPage";

function App() {
  useEffect(() => {
    TokenManager.initialize();
  }, []);

  return (
    <Router>
      <Routes>
        {/* Public routes */}
        <Route element={<ProtectedRoute isPublic={true} />}>
          <Route path="/" element={<HomePage />} />
          <Route path="/ping" element={<NotFoundPage />} />
          <Route path="/search" element={<SearchPage />} />
          <Route path="/all-cars" element={<AllCars />} />
          <Route path="/discount-plans" element={<DiscountPlansPage />} />
        </Route>

        {/* Guest-only routes */}
        <Route element={<ProtectedRoute isRestrictedToGuests={true} />}>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
        </Route>

        {/* Routes for logged-in users */}
        <Route element={<ProtectedRoute />}>
          <Route path="/logout" element={<LogoutPage />} />
          <Route path="/pay-handle" element={<PaymentHandlerPage />} />
          <Route path="/my-bookings" element={<AllBookings />} />
          <Route path="/all-auctions" element={<AllAuctionsPage />} />
          <Route path="/auction" element={<AuctionPage />} />
          <Route path="/my-profile" element={<ProfilePage />} />
          <Route path="/claim-auction" element={<ClaimAuctionPage />} />
        </Route>

        {/* CUSTOMER-specific routes */}
        <Route element={<ProtectedRoute requiredRole="CUSTOMER" />}>
          <Route path="/new-booking" element={<BookingPage />} />
        </Route>

        {/* ADMIN-specific routes */}
        <Route element={<ProtectedRoute requiredRole="ADMIN" />}>
          <Route path="/my-payments" element={<MyPaymentsPage />} />
          <Route path="/manage-car" element={<ManageCarPage />} />
          <Route path="/create-discount" element={<CreateDiscountPlanPage />} />
          <Route path="/create-auction" element={<CreateAuctionPage />} />
          <Route path="/car-info" element={<CarInfoPage />} />
          <Route path="/statistics" element={<StatisticsPage />} />
          <Route path="/all-messages" element={<AllMessagesPage />} />
          <Route path="/deployments" element={<DeploymentsPage />} />
        </Route>

        <Route path="*" element={<NotFoundPage />} />
      </Routes>
    </Router>
  );
}

export default App;
