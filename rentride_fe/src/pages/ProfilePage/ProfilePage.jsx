import React, { useEffect, useState } from "react";
import NavBar from "../../components/NavBar/NavBar";
import ProfileSettings from "./ProfileSettings";
import {
  getCurrentUserProfile,
  updateUserEmailSettings,
} from "../../api/userService";
import LoadingSpinner from "../../components/LoadingSpinner/LoadingSpinner";
import {
  getAllBookingsCount,
  getPagedUserBookings,
} from "../../api/bookingService";
import AllBookingsTable from "../../components/AllBookingsTable/AllBookingsTable";
import {
  getAllPaymentsCount,
  getPagedUserPayments,
} from "../../api/paymentService";
import AllPaymentsTable from "../../components/AllPaymentsTable/AllPaymentsTable";
import { getUserReviews } from "../../api/reviewService";
import AllReviewsTable from "../../components/AllReviewsTable/AllReviewsTable";
import Pagination from "../../components/Pagination/Pagination";

export default function ProfilePage() {
  const [loadingState, setLoadingState] = useState({
    isLoadingUserData: true,
    isLoadingBookings: true,
    isLoadingPayments: true,
    isLoadingReviews: true,
  });
  const [userData, setUserData] = useState({});
  const [bookings, setBookings] = useState([]);
  const [payments, setPayments] = useState([]);
  const [reviews, setReviews] = useState([]);
  const [currentTab, setCurrentTab] = useState("profileSettings");

  const [totalPaymentsCount, setTotalPayments] = useState(0);
  const [totalBookingsCount, setTotalBookings] = useState(0);

  async function loadReviewsData() {
    setLoadingState((prevState) => ({ ...prevState, isLoadingReviews: true }));

    const data = await getUserReviews();
    setReviews(data);

    setLoadingState((prevState) => ({
      ...prevState,
      isLoadingReviews: false,
    }));
  }

  async function fetchAllPaymentsCount() {
    const count = await getAllPaymentsCount();
    setTotalPayments(count);
  }

  async function fetchAllBookingsCount() {
    const count = await getAllBookingsCount();
    setTotalBookings(count);
  }

  async function loadPaymentsData(page) {
    setLoadingState((prevState) => ({ ...prevState, isLoadingPayments: true }));

    const data = await getPagedUserPayments(page);
    setPayments(data);

    setLoadingState((prevState) => ({
      ...prevState,
      isLoadingPayments: false,
    }));
  }

  async function loadBookingsData(page) {
    setLoadingState((prevState) => ({ ...prevState, isLoadingBookings: true }));

    const data = await getPagedUserBookings(page);
    setBookings(data);

    setLoadingState((prevState) => ({
      ...prevState,
      isLoadingUserData: false,
    }));
  }

  async function loadProfileData() {
    setLoadingState((prevState) => ({ ...prevState, isLoadingUserData: true }));

    const data = await getCurrentUserProfile();
    setUserData(data);

    setLoadingState((prevState) => ({
      ...prevState,
      isLoadingUserData: false,
    }));
  }

  async function handleEmailSettingsChange(field, value) {
    const updatedSettings = { ...userData, [field]: value };

    setUserData(updatedSettings);

    try {
      await updateUserEmailSettings({
        bookingsEmails: updatedSettings.bookingsEmails,
        damageEmails: updatedSettings.damageEmails,
        promoEmails: updatedSettings.promoEmails,
      });
    } catch (error) {
      console.error("Failed to update email settings:", error);

      setUserData(userData);
    }
  }

  function getTabClass(tabName) {
    return currentTab == tabName ? "text-primary" : "";
  }

  function getSelectedComponenet() {
    switch (currentTab) {
      case "profileSettings":
        return (
          <ProfileSettings
            onDataChanged={loadProfileData}
            userData={userData}
          />
        );
      case "reviews":
        return <AllReviewsTable reviews={reviews} onUpdate={loadReviewsData} />;
      case "payments":
        return (
          <div>
            <AllPaymentsTable payments={payments} />

            {totalPaymentsCount > 0 && (
              <Pagination
                onPageChange={loadPaymentsData}
                totalCount={totalPaymentsCount}
              />
            )}
          </div>
        );
      case "bookings":
        return (
          <div>
            <AllBookingsTable
              bookings={bookings}
              loadCallback={loadBookingsData}
            />

            {totalBookingsCount > 0 && (
              <Pagination
                onPageChange={loadBookingsData}
                totalCount={totalBookingsCount}
              />
            )}
          </div>
        );
      default:
        "";
    }
  }
  useEffect(() => {
    loadProfileData();

    loadReviewsData();
  }, []);

  useEffect(() => {
    fetchAllBookingsCount();
    loadBookingsData(0);

    fetchAllPaymentsCount();
    loadPaymentsData(0);
  }, [currentTab]);

  return (
    <>
      <NavBar bg="light" />

      <div className="w-80 m-auto py-5 profile-page">
        <div className="row">
          <div className="col-md-3">
            {loadingState.isLoadingUserData ? (
              <LoadingSpinner />
            ) : (
              <div className="card light-gray-background text-light p-3">
                <div className="text-center">
                  <div className="rounded-circle bg-light mb-3">
                    <img
                      src={`https://api.dicebear.com/9.x/rings/svg?seed=${userData.name}`}
                      alt="Profile Avatar"
                    />
                  </div>
                  <h3>{userData.name}</h3>
                </div>
                <hr className="bg-light"></hr>
                <p>Email settings:</p>
                <div className="form-check">
                  <input
                    className="form-check-input"
                    type="checkbox"
                    id="bookings"
                    checked={userData.bookingsEmails || false}
                    onChange={(e) =>
                      handleEmailSettingsChange(
                        "bookingsEmails",
                        e.target.checked
                      )
                    }
                  />
                  <label className="form-check-label" htmlFor="bookings">
                    Bookings
                  </label>
                </div>
                <div className="form-check">
                  <input
                    className="form-check-input"
                    type="checkbox"
                    id="damageReports"
                    checked={userData.damageEmails || false}
                    onChange={(e) =>
                      handleEmailSettingsChange(
                        "damageEmails",
                        e.target.checked
                      )
                    }
                  />
                  <label className="form-check-label" htmlFor="damageReports">
                    Damage reports
                  </label>
                </div>
                <div className="form-check">
                  <input
                    className="form-check-input"
                    type="checkbox"
                    id="promotional"
                    checked={userData.promoEmails || false}
                    onChange={(e) =>
                      handleEmailSettingsChange("promoEmails", e.target.checked)
                    }
                  />
                  <label className="form-check-label" htmlFor="promotional">
                    Promotional
                  </label>
                </div>
              </div>
            )}
          </div>

          <div className="col-md-9">
            <div className="text-white">
              <div className="d-flex justify-content-between w-80 m-auto p-3">
                <h3
                  role="button"
                  onClick={() => setCurrentTab("profileSettings")}
                  className={getTabClass("profileSettings")}
                >
                  Profile settings
                </h3>
                <h3
                  role="button"
                  onClick={() => setCurrentTab("payments")}
                  className={getTabClass("payments")}
                >
                  Payments
                </h3>
                <h3
                  role="button"
                  onClick={() => setCurrentTab("bookings")}
                  className={getTabClass("bookings")}
                >
                  Bookings
                </h3>
                <h3
                  role="button"
                  onClick={() => setCurrentTab("reviews")}
                  className={getTabClass("reviews")}
                >
                  Reviews
                </h3>
              </div>

              <hr className="white-hr"></hr>

              {loadingState.isLoadingUserData ||
              loadingState.isLoadingUserData ? (
                <LoadingSpinner />
              ) : (
                getSelectedComponenet()
              )}
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
