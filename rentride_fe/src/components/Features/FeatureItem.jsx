import React from "react";
import styles from "./FeatureItem.module.css";

export default function FeatureItem(props) {
  return (
    <div className={`${styles.featureItem} text-white round-edge p-4`}>
      <p className="text-bold">{props.feature.title}</p>
      <div className="d-flex">
        <i className={`${props.feature.icon} ${styles.featureIcon}`}></i>
        <p>{props.feature.text}</p>
      </div>
    </div>
  );
}
