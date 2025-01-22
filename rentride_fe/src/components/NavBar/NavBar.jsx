import React from "react";
import { NavLink } from "react-router-dom";
import {
  showCityInputPopup,
  showCityConfirmPopup,
} from "../../api/utils/swalHelpers";
import { lookupCity, createCity } from "../../api/cityService";
import Swal from "sweetalert2";
import { isCurrentUserInRole, isUserLoggedIn } from "../../api/authService";
import { showInputPopup } from "../../api/utils/swalHelpers";
import { submitMessage } from "../../api/messagesService";

export default function NavBar(props) {
  const links = [
    {
      id: 1,
      path: "/discount-plans",
      text: "Discount Plans",
    },
  ];

  if (!isUserLoggedIn()) {
    links.push({
      id: links.length + 1,
      path: "/login",
      text: "Login",
    });

    links.push({
      id: links.length + 1,
      path: "/register",
      text: "Register",
    });
  } else {
    links.push({
      id: links.length + 1,
      path: "/all-auctions",
      text: "Auctions",
    });
  }

  const initiateCityCreation = async () => {
    try {
      const inputValue = await showCityInputPopup();
      if (inputValue === null) return;

      const responseData = await lookupCity(inputValue.cityName);
      if (!responseData) return;

      const isConfirmed = await showCityConfirmPopup(responseData);
      if (isConfirmed) {
        await createCity(responseData, inputValue.depotAddress);
        Swal.fire("Success", "City has been created!", "success");
      }
    } catch (error) {
      Swal.fire("Error", error.message, "error");
      console.error(error);
    }
  };

  async function initiateSupportCreation(title) {
    const inputValue = await showInputPopup(title);
    if (inputValue === null) return;

    await submitMessage({ messageContent: inputValue });
    Swal.fire("Success", "Message has been sent!", "success");
  }

  function handleSupportClick() {
    initiateSupportCreation(
      "Write down your message and a support representative will shortly reply"
    );
  }

  const navColors =
    props.bg == "light"
      ? "bg-secondary text-white"
      : " bg-white light-grey-color";

  return (
    <nav
      className={`text-white d-flex align-items-center mb-3 gap-25 p-3 text-bold ${props.bg}-gray-background`}
    >
      <NavLink
        key={"home-link"}
        to={"/"}
        className={`nav-link p-3 ${navColors} round-edge `}
      >
        <i className="fa-solid fa-location-arrow"></i> Rent Ride
      </NavLink>

      {links.map((link) => {
        return (
          <NavLink
            key={link.id}
            to={link.path}
            className={`nav-link p-2 round-edge ${navColors}`}
          >
            {link.text}{" "}
          </NavLink>
        );
      })}

      {isUserLoggedIn() ? (
        <button
          onClick={handleSupportClick}
          className={`nav-link p-2 ${navColors} round-edge `}
        >
          Ask for help
        </button>
      ) : (
        ""
      )}

      {isCurrentUserInRole("ADMIN") ? (
        <div className="dropdown">
          <button
            className={`btn ${navColors} dropdown-toggle`}
            type="button"
            id="dropdownMenuButton1"
            data-bs-toggle="dropdown"
            aria-expanded="false"
          >
            Admin tools
          </button>
          <ul className="dropdown-menu" aria-labelledby="dropdownMenuButton1">
            <li>
              <a
                onClick={initiateCityCreation}
                className="dropdown-item"
                href="#"
              >
                Create city
              </a>
            </li>
            <li>
              <NavLink className="dropdown-item" to="/manage-car">
                Create car
              </NavLink>
              <NavLink className="dropdown-item" to="/create-discount">
                Create Discount plan
              </NavLink>
              <NavLink
                className="dropdown-item create-auction-link"
                to="/create-auction"
              >
                Create auction
              </NavLink>
              <NavLink className="dropdown-item" to="/all-cars">
                All cars
              </NavLink>
              <NavLink className="dropdown-item" to="/my-payments">
                All payments
              </NavLink>
              <NavLink className="dropdown-item" to="/all-messages">
                All support messages
              </NavLink>
              <NavLink className="dropdown-item" to="/statistics">
                Statistics
              </NavLink>

              <NavLink className="dropdown-item" to="/deployments">
                Deployments
              </NavLink>

              <NavLink
                className="dropdown-item"
                to="/search?fromCity=1&fromDate=2024-11-12&fromTime=22%3A22&toCity=2&toDate=2025-12-12&toTime=22%3A22&filters="
              >
                Prepared Search
              </NavLink>
            </li>
          </ul>
        </div>
      ) : (
        ""
      )}

      {isUserLoggedIn() ? (
        <div className="ml-auto align-items-center d-flex gap-25">
          <NavLink
            className={`nav-link p-2 round-edge ${navColors}`}
            to="/logout"
          >
            Logout
          </NavLink>

          <NavLink
            key={`my-prof-link`}
            to={`/my-profile`}
            className="nav-link text-white "
          >
            <i className="fa-regular text-big fa-circle-user"></i>
          </NavLink>
        </div>
      ) : (
        ""
      )}
    </nav>
  );
}
