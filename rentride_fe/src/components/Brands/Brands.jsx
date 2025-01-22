import React from "react";
import styles from "./Brands.module.css";

export default function Brands() {
  const brands = [
    {
      key: 1,
      url: "https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/BMW.svg/2048px-BMW.svg.png",
    },
    {
      key: 2,
      url: "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e8/Tesla_logo.png/600px-Tesla_logo.png",
    },
    {
      key: 3,
      url: "https://www.carlogos.org/logo/Renault-logo-2015-2048x2048.png",
    },
    {
      key: 4,
      url: "https://upload.wikimedia.org/wikipedia/commons/9/90/Mercedes-Logo.svg",
    },
    {
      key: 6,
      url: "https://upload.wikimedia.org/wikipedia/en/thumb/7/78/Jaguar_logo_2021.svg/180px-Jaguar_logo_2021.svg.png",
    },
    {
      key: 7,
      url: "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6d/Volkswagen_logo_2019.svg/150px-Volkswagen_logo_2019.svg.png",
    },
  ];

  return (
    <div className={styles.brandsWrapper}>
      <h5 className="text-white text-bold">
        Rent out of the best brands in the world
      </h5>
      <br />
      <div className={styles.imagesHolder}>
        {brands.map((brandLink) => {
          return (
            <img
              key={brandLink.key}
              className={styles.img}
              src={brandLink.url}
            ></img>
          );
        })}
      </div>
    </div>
  );
}
