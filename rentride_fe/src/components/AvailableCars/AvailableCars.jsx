import React from "react";
import style from "./AvailableCars.module.css";
import CarComponent from "../CarComponent/CarComponent";

export default function AvailableCars(props) {
  let cars = props.cars;
  return (
    <div className="round-edge light-gray-background w-100 p-3">
      <h3 className="text-center">Available cars</h3>

      <br />

      <div className="d-flex gap-25 flex-column">
        {
          (cars =
            null || !cars.length
              ? "No available cars, try different search paramenters"
              : cars.map((c) => {
                  return (
                    <CarComponent
                      carDeliveryChosen={props.carDeliveryChosen}
                      routeData={props.routeData}
                      key={c.id}
                      car={c}
                    />
                  );
                }))
        }
      </div>
    </div>
  );
}
