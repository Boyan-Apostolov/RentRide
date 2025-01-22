import React from "react";
import { CONSTANTS } from "../../consts";

export default function CarFeature(props) {
  const feature = props.feature;

  return (
    <div className="d-flex light-grey-color text-bold justify-content-center fb-40 align-items-center">
      <i className={`${CONSTANTS.featureIcons[feature.featureType]} mr-2`}></i>
      <span>{feature.featureText}</span>
    </div>
  );
}
