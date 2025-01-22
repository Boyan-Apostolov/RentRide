import React, { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import NavBar from "../../components/NavBar/NavBar";
import CarComponent from "../../components/CarComponent/CarComponent";
import LoadingSpinner from "../../components/LoadingSpinner/LoadingSpinner";
import { getCarDetails } from "../../api/carService";
import StatisticBubbleComponent from "../../components/StatisticBubbleComponent/StatisticBubbleComponent";
import { CONSTANTS } from "../../consts";
import { getCarStatistics } from "../../api/statisticsService";
import AllBookingsTable from "../../components/AllBookingsTable/AllBookingsTable";
import { getCarBookings, getCarHistoryMap } from "../../api/bookingService";
export default function CarInfoPage() {
  const searchParams = new URLSearchParams(location.search);
  const carId = searchParams.get("carId");

  const statisticsIcons = CONSTANTS.statisticsIcon;

  const [car, setCar] = useState({});
  const [totals, setTotals] = useState({});
  const [bookings, setBookings] = useState([]);
  const [carHistoryMap, setCarHistoryMap] = useState("");

  const [loadingState, setLoadingState] = useState({
    isLoadingCar: true,
    isLoadingTotals: true,
    isLoadingBookings: true,
    isLoadingCarHistoryMap: true,
  });

  async function fetchBookingData() {
    setLoadingState((prevState) => ({ ...prevState, isLoadingBookings: true }));

    const bookingsData = await getCarBookings(carId);
    setBookings(bookingsData);

    setLoadingState((prevState) => ({
      ...prevState,
      isLoadingBookings: false,
    }));
  }

  async function fetchCarTotals() {
    setLoadingState((prevState) => ({ ...prevState, isLoadingTotals: true }));

    const totals = await getCarStatistics(carId);
    setTotals(totals);

    setLoadingState((prevState) => ({ ...prevState, isLoadingTotals: false }));
  }

  async function fetchCarHistoryMap() {
    setLoadingState((prevState) => ({
      ...prevState,
      isLoadingCarHistoryMap: true,
    }));

    const carHistoryMapUrl = await getCarHistoryMap(carId);
    setCarHistoryMap(carHistoryMapUrl);

    setLoadingState((prevState) => ({
      ...prevState,
      isLoadingCarHistoryMap: false,
    }));
  }

  async function fetchCarDetails() {
    setLoadingState((prevState) => ({ ...prevState, isLoadingCar: true }));

    const carData = await getCarDetails(carId);
    setCar(carData);

    setLoadingState((prevState) => ({ ...prevState, isLoadingCar: false }));
  }

  useEffect(() => {
    fetchCarDetails();
    fetchCarTotals();
    fetchBookingData();
    fetchCarHistoryMap();
  }, []);

  return (
    <div className="carinfo-page">
      <NavBar bg="light" />
      <h2 className="text-center text-white mt-5">Car Details page</h2>

      <div className="d-flex justify-content-around align-items-center gap-25 p-3 m-3">
        <div className="fb-50">
          <br></br>
          {loadingState.isLoadingCar ? (
            <LoadingSpinner />
          ) : (
            <CarComponent car={car} excludePrice={true} />
          )}
        </div>

        <div className="round-edge m-3 p-3 fb-40 text-white d-flex gap-25 flex-wrap">
          {loadingState.isLoadingTotals ? (
            <LoadingSpinner />
          ) : (
            <>
              <StatisticBubbleComponent
                title="Total Bookings"
                className="fb-45"
                value={totals.totalBookings || 0}
                icon={statisticsIcons.appointment}
              />

              <StatisticBubbleComponent
                title="Total Distance"
                className="fb-45"
                value={`${totals.totalDistance || 0} km.`}
                icon={statisticsIcons.distance}
              />

              <StatisticBubbleComponent
                title="Total Revenue"
                className="fb-45"
                value={`${totals.totalRevenue || 0} €`}
                icon={statisticsIcons.revenue}
              />

              <StatisticBubbleComponent
                title="Average rating"
                className="fb-45"
                value={`${totals.averageRating || 0} / 5`}
                icon={statisticsIcons.rating}
              />
            </>
          )}
        </div>
      </div>

      <div className="d-flex justify-content-around align-items-center gap-25 p-3 m-3">
        <div className="fb-50 overflow-scroll">
          <br></br>
          {loadingState.isLoadingBookings ? (
            <LoadingSpinner />
          ) : (
            <AllBookingsTable bookings={bookings} />
          )}
        </div>

        {loadingState.isLoadingCarHistoryMap ? (
          <LoadingSpinner />
        ) : (
          <div className="round-edge m-3 p-3 light-gray-background text-white fb-45 text-center">
            <h3>Car History</h3>

            <img
              className={`w-100 img fb-70 round-edge`}
              src={carHistoryMap}
            ></img>
          </div>
        )}
      </div>
    </div>
  );
}
