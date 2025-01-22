import React, { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import NavBar from "../../components/NavBar/NavBar";
import CarComponent from "../../components/CarComponent/CarComponent";
import LoadingSpinner from "../../components/LoadingSpinner/LoadingSpinner";
import { getCarDetails } from "../../api/carService";
import { getCity } from "../../api/cityService";
import CoverageOptions from "../../components/CoverageOptions/CoverageOptions";
import ReviewComponent from "../../components/ReviewComponent/ReviewComponent";
import { getPriceDetails } from "../../api/bookingService";
import { getPaymentLink } from "../../api/paymentService";
import { createBooking } from "../../api/bookingService";
import { getCarReviews } from "../../api/reviewService";
import { getCurrentUserId } from "../../api/authService";

export default function BookingPage() {
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);

  const fromCity = searchParams.get("fromCity");
  const fromDate = searchParams.get("fromDate");
  const fromTime = searchParams.get("fromTime");
  const toCity = searchParams.get("toCity");
  const toDate = searchParams.get("toDate");
  const toTime = searchParams.get("toTime");
  const carId = searchParams.get("carId");
  const deliveryAddress = searchParams.get("delivery");

  const [car, setCar] = useState({});
  const [citiesData, setCitiesData] = useState({ fromCity: "", toCity: "" });
  const [priceData, setPriceData] = useState({});
  const [reviewsData, setReviewsData] = useState({});

  const [loadingState, setLoadingState] = useState({
    isLoadingCar: true,
    isLoadingCities: true,
    isLoadingPrices: true,
    isLoadingPayment: false,
    isLoadingReviews: true,
  });

  async function fetchReviews() {
    setLoadingState((prevState) => ({ ...prevState, isLoadingReviews: true }));

    const reviewsData = await getCarReviews(carId);
    setReviewsData(reviewsData);

    setLoadingState((prevState) => ({ ...prevState, isLoadingReviews: false }));
  }

  async function fetchPrices() {
    setLoadingState((prevState) => ({ ...prevState, isLoadingPrices: true }));

    const priceData = await getPriceDetails({
      carId,
      fromCityId: fromCity,
      toCityId: toCity,
      fromDateTime: `${fromDate}T${fromTime}`,
      toDateTime: `${toDate}T${toTime}`,
    });
    setPriceData(priceData);

    setLoadingState((prevState) => ({ ...prevState, isLoadingPrices: false }));
  }

  async function fetchCarDetails() {
    setLoadingState((prevState) => ({ ...prevState, isLoadingCar: true }));

    const carData = await getCarDetails(carId);
    setCar(carData);

    setLoadingState((prevState) => ({ ...prevState, isLoadingCar: false }));
  }

  async function fetchCityDetails() {
    setLoadingState((prevState) => ({ ...prevState, isLoadingCities: true }));

    const fromCityData = await getCity(fromCity);
    const toCityData = await getCity(toCity);

    setCitiesData({
      fromCity: fromCityData.name,
      fromCityAddress: fromCityData.depoAddress,
      toCity: toCityData.name,
      toCityAddress: toCityData.depoAddress,
    });

    setLoadingState((prevState) => ({ ...prevState, isLoadingCities: false }));
  }

  useEffect(() => {
    fetchCarDetails();
    fetchCityDetails();
    fetchPrices();
    fetchReviews();
  }, []);

  const getTotal = () =>
    (priceData.dynamicTotal || priceData.total) + (deliveryAddress ? 10 : 0);

  async function handleBookingCreation() {
    setLoadingState((prevState) => ({ ...prevState, isLoadingPayment: true }));
    const bookingObj = {
      carId: carId,
      fromCityId: fromCity,
      toCityId: toCity,
      fromDate: fromDate,
      startDateTime: `${fromDate}T${fromTime}`,
      endDateTime: `${toDate}T${toTime}`,
      totalPrice: getTotal().toFixed(2),
      coverage:
        priceData.coverageFee == 0 ? 0 : priceData.coverageFee == 10 ? 1 : 2,
      userId: getCurrentUserId(),
    };

    let bookingId = await createBooking(bookingObj);
    handlePayment(bookingId);
  }

  async function handlePayment(bookingId) {
    let paymentLink = await getPaymentLink(bookingId);
    setLoadingState((prevState) => ({ ...prevState, isLoadingPayment: false }));
    document.location.href = paymentLink;
  }

  function handleSelectedOptionPrice(price) {
    setPriceData((prevData) => ({
      ...prevData,
      coverageFee: price,
      dynamicTotal: prevData.total + price,
    }));
  }

  return (
    <div className="booking-page">
      <NavBar bg="light" />

      <div className="d-flex justify-content-around align-items-center gap-25 p-3 m-3">
        <div className="fb-50">
          <h3 className="text-center text-white">Preview Your New booking</h3>
          <br></br>
          {loadingState.isLoadingCar ? (
            <LoadingSpinner />
          ) : (
            <CarComponent car={car} excludePrice={true} />
          )}
        </div>

        {loadingState.isLoadingPrices ? (
          <LoadingSpinner />
        ) : (
          <div className="round-edge m-3 p-3 fb-40 light-gray-background text-white">
            <h3 className="text-center">Please, select coverage</h3>
            <CoverageOptions
              setSelectedOptionPrice={handleSelectedOptionPrice}
            />
          </div>
        )}
      </div>

      <div className="d-flex justify-content-around gap-25 p-3">
        <div className="round-edge p-3 fb-50 light-gray-background text-white">
          <h3 className="text-center">Latest car reviews</h3>
          {loadingState.isLoadingReviews ? (
            <LoadingSpinner />
          ) : !reviewsData ? (
            "No reviews yet"
          ) : (
            reviewsData.map((r) => {
              return (
                <ReviewComponent
                  key={r.id}
                  review={r}
                  onUpdate={fetchReviews}
                />
              );
            })
          )}
        </div>

        {loadingState.isLoadingCities ? (
          <LoadingSpinner />
        ) : (
          <div className="round-edge p-3 fb-40 light-gray-background text-white">
            <h3 className="text-center">Booking details</h3>
            <h4>Pick-Up:</h4>
            <div className="d-flex justify-content-around text-white from-city-text">
              <span>
                <i className="fa-solid fa-location-dot m-2 "></i>
                {citiesData.fromCity}
              </span>
              <span>
                <i className="fa-regular fa-calendar m-2"></i>
                {fromDate}
              </span>
              <span>
                <i className="fa-regular fa-clock m-2"></i>
                {fromTime}
              </span>
            </div>
            <span>
              Address: {deliveryAddress || citiesData.fromCityAddress}
            </span>
            <hr className="white-hr"></hr>
            <h4>Drop-Off:</h4>
            <div className="d-flex justify-content-around text-white to-city-text">
              <span>
                <i className="fa-solid fa-location-dot m-2"></i>
                {citiesData.toCity}
              </span>
              <span>
                <i className="fa-regular fa-calendar m-2"></i>
                {toDate}
              </span>
              <span>
                <i className="fa-regular fa-clock m-2"></i>
                {toTime}
              </span>
            </div>
            <span>Address: {citiesData.toCityAddress}</span>
            <hr className="white-hr"></hr>
            {loadingState.isLoadingPrices ? (
              <LoadingSpinner />
            ) : (
              <div className="d-flex flex-column">
                <div className="d-flex justify-content-between">
                  <span>Fuel Costs</span>
                  <span className="text-warning text-bold">
                    {priceData.fuelCost.toFixed(2)} €
                  </span>
                </div>

                <div className="d-flex justify-content-between">
                  <span>Toll Fees</span>
                  <span className="text-warning text-bold">
                    {priceData.tollFees.toFixed(2)} €
                  </span>
                </div>

                <div className="d-flex justify-content-between">
                  <span>Service Fees</span>
                  <span className="text-warning text-bold">
                    {priceData.serviceFees.toFixed(2)} €
                  </span>
                </div>

                <div className="d-flex justify-content-between">
                  <span>Time Fees</span>
                  <span className="text-warning text-bold">
                    {priceData.timeFees.toFixed(2)} €
                  </span>
                </div>

                <div className="d-flex justify-content-between">
                  <span>Coverage</span>
                  <span className="text-warning text-bold">
                    {priceData.coverageFee.toFixed(2)} €
                  </span>
                </div>

                <div className="d-flex justify-content-between">
                  <span>User discount</span>
                  <span className="text-warning text-bold">
                    {priceData.userDiscount.toFixed(2)} €
                  </span>
                </div>

                {!deliveryAddress ? (
                  ""
                ) : (
                  <div className="d-flex justify-content-between">
                    <span>Custom delivery address</span>
                    <span className="text-warning text-bold">10.00 €</span>
                  </div>
                )}

                <hr className="white-hr"></hr>

                <div className="d-flex justify-content-between text-success text-bold fs-25 total-amount">
                  <span>Total amount</span>
                  <span>{getTotal().toFixed(2)} €</span>
                </div>
                {loadingState.isLoadingPayment ? (
                  <LoadingSpinner />
                ) : (
                  <button
                    onClick={handleBookingCreation}
                    className="btn round-edge btn-white text-bold bg-success text-white"
                  >
                    PAY NOW
                  </button>
                )}
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
}
