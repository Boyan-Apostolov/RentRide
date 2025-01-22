import React from "react";
import styles from "./Features.module.css";
import FeatureItem from "./FeatureItem";

export default function Features() {
  const features = [
    {
      id: 1,
      title: "Wide Vehicle Selection",
      text: "Diverse fleet for all preferences",
      icon: "fas fa-car",
    },
    {
      id: 2,
      title: "Flexible Payments",
      text: "Multiple payment options for your convenience",
      icon: "fas fa-credit-card",
    },
    {
      id: 3,
      title: "Real-time Booking",
      text: "Dynamic pricing and instant booking",
      icon: "fas fa-clock",
    },
    {
      id: 4,
      title: "Easy management",
      text: "Book and manage rentals on the go",
      icon: "fas fa-mobile-alt",
    },
  ];
  return (
    <div className={styles.featuresWrapper}>
      {features.map((feature) => {
        return <FeatureItem key={feature.id} feature={feature} />;
      })}
      ;
    </div>
  );
}
