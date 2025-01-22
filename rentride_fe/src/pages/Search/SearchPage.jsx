import React, { useState, useEffect } from "react";
import styles from "./SearchPage.module.css";
import { useLocation } from "react-router-dom";
import SearchFilter from "../../components/SearchFilter/SearchFilter";
import NavBar from "../../components/NavBar/NavBar";
import { getRouteData } from "../../api/cityService";
import { getAvailableCars } from "../../api/carService";
import AvailableCars from "../../components/AvailableCars/AvailableCars";
import CarFeaturesFilter from "../../components/CarFeaturesFilter/CarFeaturesFilter";
import LoadingSpinner from "../../components/LoadingSpinner/LoadingSpinner";

export default function SearchPage() {
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);

  const fromCity = searchParams.get("fromCity");
  const fromDate = searchParams.get("fromDate");
  const fromTime = searchParams.get("fromTime");
  const toCity = searchParams.get("toCity");
  const toDate = searchParams.get("toDate");
  const toTime = searchParams.get("toTime");
  const filters = searchParams.get("filters");

  const [route, setRoute] = useState({
    fromCity: 0,
    toCity: 0,
    distance: 0,
    time: 0,
    imgUrl: "",
  });

  const [carDeliveryChosen, setDeliveryChosen] = useState(false);
  const [carData, setCarData] = useState([]);

  const [isLoadingRoute, setIsLoadingRoute] = useState(true);
  const [isLoadingCars, setIsLoadingCars] = useState(true);

  async function fetchPageData() {
    await fetchRouteData();
    await fetchCarData();
  }

  async function fetchRouteData() {
    if (fromCity && toCity) {
      setIsLoadingRoute(true);
      const routeData = await getRouteData(fromCity, toCity);
      setIsLoadingRoute(false);
      setRoute(routeData);
    }
  }

  async function fetchCarData() {
    setIsLoadingCars(true);
    const carData = await getAvailableCars(
      fromCity,
      fromDate,
      fromTime,
      toCity,
      toDate,
      toTime,
      filters
    );
    setIsLoadingCars(false);
    setCarData(carData);
  }

  useEffect(() => {
    fetchRouteData();
  }, [fromCity, toCity, toDate, toTime, toTime]);

  useEffect(() => {
    fetchCarData();
  }, [filters]);

  return (
    <>
      <NavBar bg="light" />
      <div className={styles.filterPageWrapper}>
        <br />
        <div className="d-flex justify-content-between">
          <div className={`${styles.leftColumn} gap-25`}>
            <SearchFilter callback={() => fetchPageData()} />
            <CarFeaturesFilter cars={carData} callback={() => fetchCarData()} />
          </div>

          <div className={styles.rightColumn}>
            {isLoadingRoute ? (
              <LoadingSpinner />
            ) : (
              <>
                <div className="d-flex gap-25 align-items-stretch">
                  <div className={`round-edge light-gray-background p-4 w-100`}>
                    <h3 className="text-center">Your route</h3>
                    <div className="d-flex gap-25">
                      <img
                        className={`w-100 ${styles.fb70} ${styles.img} round-edge`}
                        src={route.imgUrl}
                      ></img>
                      <div className={`${styles.routeInfo} text-primary`}>
                        <p>{route.distance} km</p>
                        <p>{route.time} h</p>
                      </div>
                    </div>
                  </div>

                  <div className="round-edge light-gray-background p-3">
                    <h3 className="text-center">Cannot come to our parking?</h3>
                    <br />
                    <div className="d-flex align-items-center justify-content-evenly">
                      <input
                        type="checkbox"
                        id="extra-fee"
                        className="form-check-input w-25 h-50p"
                        checked={carDeliveryChosen}
                        onChange={() => setDeliveryChosen(!carDeliveryChosen)}
                      />
                      <label htmlFor="extra-fee" className="fs-25">
                        Car delivery
                      </label>
                    </div>
                    <br />
                    <br />
                    <hr />
                    <span className="font-italic">
                      Note: a fee of 10€ will be applied if you choose this
                      option
                    </span>
                  </div>
                </div>

                <br />
              </>
            )}
            {isLoadingCars ? (
              <LoadingSpinner />
            ) : (
              <AvailableCars
                carDeliveryChosen={carDeliveryChosen}
                routeData={route}
                cars={carData}
              />
            )}
          </div>
        </div>
      </div>
    </>
  );
}
