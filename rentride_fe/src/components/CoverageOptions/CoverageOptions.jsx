import React, { useState } from "react";

export default function CoverageOptions(props) {
  const [selectedOption, setSelectedOption] = useState("noCoverage");

  const handleSelect = (option) => {
    setSelectedOption(option);
    const price =
      option == "topProtection" ? 10 : option == "premiumProtection" ? 20 : 0;
    props.setSelectedOptionPrice(price);
  };

  return (
    <div className="container p-3">
      <div
        className={`card very-light-gray-background mb-3 p-3 ${
          selectedOption === "noCoverage" ? "border-primary" : ""
        }`}
        onClick={() => handleSelect("noCoverage")}
        style={{ cursor: "pointer" }}
      >
        <div className="d-flex justify-content-between align-items-center">
          <div>
            <input
              type="radio"
              checked={selectedOption === "noCoverage"}
              onChange={() => handleSelect("noCoverage")}
            />
            <span className="ms-2 text-danger fw-bold">No coverage</span>
          </div>
          <div>
            <span className="fw-bold">0.00 €</span>
            <button className="btn btn-outline-danger ms-3">
              {selectedOption === "noCoverage" ? "Selected" : "Select"}
            </button>
          </div>
        </div>
        <span className="ms-2 text-danger fw-bold">
          You are liable for all damages
        </span>
      </div>

      <div
        className={`card very-light-gray-background mb-3 p-3 ${
          selectedOption === "topProtection" ? "border-primary" : ""
        }`}
        onClick={() => handleSelect("topProtection")}
        style={{ cursor: "pointer" }}
      >
        <div className="d-flex justify-content-between align-items-center">
          <div>
            <input
              type="radio"
              checked={selectedOption === "topProtection"}
              onChange={() => handleSelect("topProtection")}
            />
            <span className="ms-2 text-primary fw-bold">Top</span>
          </div>
          <div>
            <span className="fw-bold">10.00 €</span>
            <button className="btn btn-outline-primary ms-3">
              {selectedOption === "topProtection" ? "Selected" : "Select"}
            </button>
          </div>
        </div>
        <span className="ms-2 text-primary fw-bold">
          You are liable for half of all damages
        </span>
      </div>

      <div
        className={`card very-light-gray-background mb-3 p-3 ${
          selectedOption === "premiumProtection" ? "border-primary" : ""
        }`}
        onClick={() => handleSelect("premiumProtection")}
        style={{ cursor: "pointer" }}
      >
        <div className="d-flex justify-content-between align-items-center">
          <div>
            <input
              type="radio"
              checked={selectedOption === "premiumProtection"}
              onChange={() => handleSelect("premiumProtection")}
            />
            <span className="ms-2 text-warning fw-bold">Premium</span>
          </div>
          <div>
            <span className="fw-bold">20.00 €</span>
            <button className="btn btn-outline-warning ms-3">
              {selectedOption === "premiumProtection" ? "Selected" : "Select"}
            </button>
          </div>
        </div>
        <span className="ms-2 text-warning fw-bold">
          You are NOT liable for any damages
        </span>
      </div>
    </div>
  );
}
