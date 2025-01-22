import React from "react";
import style from "./CarComponent.module.css";
import CarFeature from "../CarFeature/CarFeature";
import { NavLink, useSearchParams } from "react-router-dom";
import { showInputPopup } from "../../api/utils/swalHelpers";
import { useNavigate } from "react-router-dom";
import { isUserLoggedIn } from "../../api/authService";

export default function CarComponent(props) {
  const navigate = useNavigate();

  const car = props.car;
  const [searchParams, setSearchParams] = useSearchParams();

  const personalizedPrice =
    Math.ceil(car.fuelPrice) + (props.carDeliveryChosen ? 10 : 0);

  function handleStartBooking() {
    const bookingUrl = `/new-booking?carId=${car.id}&`;

    if (props.carDeliveryChosen) {
      showInputPopup("Enter your custom delivery address").then(
        (inputValue) => {
          if (!inputValue) return;

          navigate(
            `${bookingUrl}${searchParams.toString()}&delivery=${inputValue}`
          );
        }
      );
    } else {
      navigate(`${bookingUrl}${searchParams.toString()}`);
    }
  }

  return (
    <div className="car-component d-flex round-edge very-light-gray-background p-3 gap-25 justify-content-around">
      <div className={`${style.fb30}`}>
        <h4 className="text-center text-bold">
          {car.make} - {car.model}
        </h4>
        <img className="round-edge" src={car.photosBase64[0]} />
      </div>
      <div
        className={`${style.fb30} ${style.featureWrapper} d-flex flex-column`}
      >
        <div className="d-flex flex-wrap">
          {car.carFeatures.map((f) => {
            return <CarFeature key={f.id} feature={f} />;
          })}
        </div>
        <br></br>
        <div>
          <i className="fa-solid fa-star"></i>
          <i className="fa-solid fa-star"></i>
          <i className="fa-solid fa-star"></i>
          <i className="fa-solid fa-star"></i>
          <i className="fa-solid fa-star"></i>
        </div>
      </div>

      {props.excludePrice ? (
        ""
      ) : (
        <div className={`${style.fb30} d-flex flex-column text-center`}>
          <h3 className="text-bold">Personalized Price</h3>
          <span className="font-italic">Excluding fees & insurance</span>
          <br></br>
          <h2 className="text-primary text-big">{personalizedPrice} €</h2>
          <br></br>
          {!personalizedPrice || !isUserLoggedIn() ? (
            ""
          ) : (
            <button
              className="btn round-edge btn-white text-bold bg-primary text-white"
              data-bookbthcarid={car.id}
              onClick={handleStartBooking}
            >
              Start booking
            </button>
          )}

          {!isUserLoggedIn() ? (
            <NavLink key="1" to="/login" className="nav-link text-white">
              <button className="btn round-edge btn-white text-bold bg-primary text-white">
                Login to book
              </button>
            </NavLink>
          ) : (
            ""
          )}
        </div>
      )}
    </div>
  );
}
