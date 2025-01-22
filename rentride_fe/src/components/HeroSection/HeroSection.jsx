import React from "react";
import NavBar from "../NavBar/NavBar";
import SearchFilter from "../SearchFilter/SearchFilter";
import styles from "./HeroSection.module.css";

export default function HeroSection() {
  return (
    <div className={styles.hero}>
      <NavBar />

      <div className="hero-content d-flex justify-content-around align-items-center">
        <div className="welcome d-flex text-bold flex-column">
          <div className="d-flex align-items-center fs-25">
            <div className={`${styles.logo} mr-2 round-edge`}>
              <i className="fa-solid fa-location-arrow"></i>
              <span> Rent Ride </span>
            </div>
            <div className="title text-white">: Easy, Fast, Yours</div>
          </div>
          <div className="text-white fs-20">
            Instant access to the car you need, whenever you need it.
          </div>
        </div>

        <SearchFilter callback={() => {}} />
      </div>
      <br />
      <br />
      <br />
    </div>
  );
}
