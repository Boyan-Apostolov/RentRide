import React, { useState, useEffect } from "react";
import NavBar from "../../components/NavBar/NavBar";
import LoadingSpinner from "../../components/LoadingSpinner/LoadingSpinner";
import StatisticBubbleComponent from "../../components/StatisticBubbleComponent/StatisticBubbleComponent";
import { CONSTANTS } from "../../consts";
import {
  getGeneralStatistics,
  getMostBoughtDiscountPlans,
  getMostPopularCars,
  getMostPopularTrips,
  getBookingsPerMonth,
  getPopularCarsOverTime,
} from "../../api/statisticsService";
import ColumnChart from "../../components/ColumnChart/ChartComponent";
import { showError } from "../../api/utils/swalHelpers";
export default function StatisticsPage() {
  const statisticsIcons = CONSTANTS.statisticsIcon;

  const [generalStatistics, setGeneralStatistics] = useState({});
  const [mostBoughtDiscountPlans, setMostBoughtDiscountPlans] = useState({});
  const [mostPopularCars, setMostPopularCars] = useState({});
  const [mostPopularTrips, setMostPopularTrips] = useState({});
  const [bookingsPerMonth, setBookingsPerMonth] = useState({});
  const [popularCarsOverTime, setPopularCarsOverTime] = useState({});

  const [fromDateFilter, setFromDateFilter] = useState(null);
  const [toDateFilter, setToDateFilter] = useState(null);

  const [loadingState, setLoadingState] = useState({
    isLoadingGeneralStatistics: true,
    isLoadingMostBoughtDiscountPlans: true,
    isLoadingMostPopularCars: true,
    isLoadingMostPopularTrips: true,
    isLoadingBookingsPerMonth: true,
    isLoadingPopularCarsOverTime: true,
  });

  async function fetchPopularCarsOverTime(fromDate, toDate) {
    setLoadingState((prevState) => ({
      ...prevState,
      isLoadingPopularCarsOverTime: true,
    }));

    const bookingsPerMonth = await getPopularCarsOverTime(fromDate, toDate);
    setPopularCarsOverTime(bookingsPerMonth || []);

    setLoadingState((prevState) => ({
      ...prevState,
      isLoadingPopularCarsOverTime: false,
    }));
  }

  async function fetchBookingsPerMonth() {
    setLoadingState((prevState) => ({
      ...prevState,
      isLoadingBookingsPerMonth: true,
    }));

    const bookingsPerMonth = await getBookingsPerMonth();
    setBookingsPerMonth(bookingsPerMonth);

    setLoadingState((prevState) => ({
      ...prevState,
      isLoadingBookingsPerMonth: false,
    }));
  }

  async function fetchMostPopularTrips() {
    setLoadingState((prevState) => ({
      ...prevState,
      isLoadingMostPopularTrips: true,
    }));

    const mostPopularTrips = await getMostPopularTrips();
    setMostPopularTrips(mostPopularTrips);

    setLoadingState((prevState) => ({
      ...prevState,
      isLoadingMostPopularTrips: false,
    }));
  }

  async function fetchGeneralStatistics() {
    setLoadingState((prevState) => ({
      ...prevState,
      isLoadingGeneralStatistics: true,
    }));

    const generalStatistics = await getGeneralStatistics();
    setGeneralStatistics(generalStatistics);

    setLoadingState((prevState) => ({
      ...prevState,
      isLoadingGeneralStatistics: false,
    }));
  }

  async function fetchMostBoughtDiscountPlans() {
    setLoadingState((prevState) => ({
      ...prevState,
      isLoadingMostBoughtDiscountPlans: true,
    }));

    const mostBoughtDiscountPlans = await getMostBoughtDiscountPlans();
    setMostBoughtDiscountPlans(mostBoughtDiscountPlans);

    setLoadingState((prevState) => ({
      ...prevState,
      isLoadingMostBoughtDiscountPlans: false,
    }));
  }

  async function fetchMostPopularCars() {
    setLoadingState((prevState) => ({
      ...prevState,
      isLoadingMostPopularCars: true,
    }));

    const mostPopularCars = await getMostPopularCars();
    setMostPopularCars(mostPopularCars);

    setLoadingState((prevState) => ({
      ...prevState,
      isLoadingMostPopularCars: false,
    }));
  }

  async function resetDateFilter() {
    setFromDateFilter(null);
    setToDateFilter(null);

    await fetchPopularCarsOverTime();
    scollToBottom();
  }
  function isBefore(startDate, endDate) {
    const start = new Date(startDate);
    const end = new Date(endDate);

    start.setHours(0, 0, 0, 0);
    end.setHours(0, 0, 0, 0);

    return start < end;
  }

  async function applyDateFilter() {
    if (
      fromDateFilter &&
      toDateFilter &&
      !isBefore(fromDateFilter, toDateFilter)
    ) {
      showError("Start date must be before end date");
      return;
    }
    await fetchPopularCarsOverTime(fromDateFilter, toDateFilter);
    scollToBottom();
  }

  function scollToBottom() {
    window.scrollTo({
      top: document.documentElement.scrollHeight,
      behavior: "smooth",
    });
  }

  useEffect(() => {
    fetchGeneralStatistics();
    fetchMostBoughtDiscountPlans();
    fetchMostPopularCars();
    fetchMostPopularTrips();
    fetchBookingsPerMonth();
    fetchPopularCarsOverTime();
  }, []);

  return (
    <div className="statistics-page">
      <NavBar bg="light" />
      <h2 className="text-center text-white mt-5">Statistics page</h2>

      <div className="d-flex gap-25 align-items-center justify-content-center mt-5">
        {loadingState.isLoadingGeneralStatistics ? (
          <LoadingSpinner />
        ) : (
          <>
            <StatisticBubbleComponent
              title="Total Bookings"
              className="fb-20"
              value={generalStatistics.totalBookings || 0}
              icon={statisticsIcons.appointment}
            />

            <StatisticBubbleComponent
              title="Total Revenue"
              className="fb-20"
              value={`${generalStatistics.totalRevenue || 0} $`}
              icon={statisticsIcons.revenue}
            />

            <StatisticBubbleComponent
              title="Total Distance"
              className="fb-20"
              value={`${generalStatistics.totalTravelDistance || 0} km`}
              icon={statisticsIcons.distance}
            />

            <StatisticBubbleComponent
              title="Total Users"
              className="fb-20"
              value={`${generalStatistics.totalUsers || 0}`}
              icon={statisticsIcons.users}
            />

            <StatisticBubbleComponent
              title="Total Reviews"
              className="fb-20"
              value={`${generalStatistics.totalReviews || 0}`}
              icon={statisticsIcons.rating}
            />
          </>
        )}
      </div>

      <div className="d-flex gap-25 m-5">
        {loadingState.isLoadingMostBoughtDiscountPlans ? (
          <LoadingSpinner />
        ) : (
          <div className="round-edge mt-5 fb-50 light-gray-background p-3 text-white">
            <h3 className="text-center">Most bought discount plans</h3>
            <ColumnChart
              type="column"
              title=""
              dataCollection={mostBoughtDiscountPlans}
            />
          </div>
        )}

        {loadingState.isLoadingMostPopularCars ? (
          <LoadingSpinner />
        ) : (
          <div className="round-edge mt-5 fb-50 light-gray-background p-3 text-white">
            <h3 className="text-center">Most popular cars</h3>
            <ColumnChart title="" type="pie" dataCollection={mostPopularCars} />
          </div>
        )}
      </div>

      <div className="d-flex gap-25 m-5">
        {loadingState.isLoadingMostPopularTrips ? (
          <LoadingSpinner />
        ) : (
          <div className="round-edge fb-50 light-gray-background p-3 text-white">
            <h3 className="text-center">Most popular trips</h3>
            <ColumnChart
              title=""
              type="pie"
              dataCollection={mostPopularTrips}
            />
          </div>
        )}

        {loadingState.isLoadingBookingsPerMonth ? (
          <LoadingSpinner />
        ) : (
          <div className="round-edge fb-50 light-gray-background p-3 text-white">
            <h3 className="text-center">Bookings per Month</h3>
            <ColumnChart
              type="line"
              title=""
              dataCollection={bookingsPerMonth}
            />
          </div>
        )}
      </div>

      <div className="d-flex justify-content-center gap-25 mt-5">
        {loadingState.isLoadingPopularCarsOverTime ? (
          <LoadingSpinner />
        ) : (
          <div className="round-edge fb-50 light-gray-background p-3 text-white">
            <h3 className="text-center">Most popular Cars over time</h3>

            <div className="m-4">
              <div className="d-flex justify-content-center align-items-end text-center gap-25">
                <div>
                  <span className="text-white">From:</span>
                  <input
                    type="date"
                    className="form-control"
                    placeholder="From"
                    value={fromDateFilter || ""}
                    onChange={(e) => setFromDateFilter(e.target.value)}
                  />
                </div>
                <div>
                  <span className="text-white">To:</span>
                  <input
                    type="date"
                    className="form-control"
                    placeholder="To"
                    value={toDateFilter || ""}
                    onChange={(e) => setToDateFilter(e.target.value)}
                  />
                </div>

                <button className="btn btn-warning" onClick={resetDateFilter}>
                  RESET
                </button>
                <button className="btn btn-success" onClick={applyDateFilter}>
                  APPLY
                </button>
              </div>
            </div>

            <ColumnChart
              title=""
              type="over-time"
              dataCollection={popularCarsOverTime}
            />
          </div>
        )}
      </div>
    </div>
  );
}
