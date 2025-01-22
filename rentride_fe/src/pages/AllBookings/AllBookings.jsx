import React, { useState, useEffect } from "react";
import { getAllBookings, getUserBookings } from "../../api/bookingService";
import NavBar from "../../components/NavBar/NavBar";
import LoadingSpinner from "../../components/LoadingSpinner/LoadingSpinner";

import AllBookingsTable from "../../components/AllBookingsTable/AllBookingsTable";
import { getCurrentUserId, isCurrentUserInRole } from "../../api/authService";

export default function AllBookings() {
  const [bookings, setBookings] = useState([]);
  const [isLoadingBookings, setLoadingBookings] = useState(true);

  useEffect(() => {
    fetchBookingData();
  }, []);

  async function fetchBookingData() {
    setLoadingBookings(true);
    const bookingsData = await (isCurrentUserInRole("ADMIN")
      ? getAllBookings()
      : getUserBookings());
    setLoadingBookings(false);
    setBookings(
      bookingsData.sort(function (a, b) {
        return b.id - a.id;
      })
    );
  }

  return (
    <>
      <NavBar bg="light" />
      {isLoadingBookings ? (
        <LoadingSpinner />
      ) : (
        <AllBookingsTable bookings={bookings} loadCallback={fetchBookingData} />
      )}
    </>
  );
}
