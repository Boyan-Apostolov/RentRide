import React, { useEffect, useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import "./CarDamageReport.css";
import { getAllDamages } from "../../api/bookingService";
import LoadingSpinner from "../LoadingSpinner/LoadingSpinner";

export default function CarDamageReportPopup(props) {
  const [isDamagesLoading, setDamagesLoading] = useState(true);
  const [damages, setDamages] = useState([]);
  const [selectedDamages, setSelectedDamages] = useState([]);
  const [totalCost, setTotalCost] = useState(0);

  async function fetchDamages() {
    setDamagesLoading(true);
    const damagesData = await getAllDamages();
    setDamages(damagesData);
    setDamagesLoading(false);
  }

  useEffect(() => {
    fetchDamages();
  }, []);

  const toggleDamageSelection = (damage) => {
    setSelectedDamages((prevSelected) => {
      const isSelected = prevSelected.includes(damage);
      const newSelectedDamages = isSelected
        ? prevSelected.filter((d) => d !== damage)
        : [...prevSelected, damage];

      // Update total cost based on selected damages
      const newTotalCost = newSelectedDamages.reduce(
        (total, d) => total + d.cost,
        0
      );
      setTotalCost(newTotalCost);

      return newSelectedDamages;
    });
  };

  const handleConfirm = () => {
    props.onConfirm(selectedDamages);
  };

  const handleCancel = () => {
    props.onClose();
  };

  return (
    <div className="popup-overlay d-flex justify-content-center align-items-center">
      <div className="popup-content p-4 bg-light rounded">
        {isDamagesLoading ? (
          <LoadingSpinner />
        ) : (
          <>
            <h1 className="text-center mb-4">Car Damage Report</h1>
            <div className="row">
              {damages.map((damage) => (
                <div key={damage.id} className="col-md-4 mb-3">
                  <div
                    className={`card cursor-pointer ${
                      selectedDamages.includes(damage) ? "border-primary" : ""
                    }`}
                    onClick={() => toggleDamageSelection(damage)}
                  >
                    <img
                      src={damage.iconUrl}
                      className="card-img-top ofcont h-80p"
                      alt={damage.name}
                    />
                    <div className="card-body text-center">
                      <h5 className="card-title">{damage.name}</h5>
                      <p className="card-text">Cost: €{damage.cost}</p>
                    </div>
                  </div>
                </div>
              ))}
            </div>
            <div className="d-flex justify-content-between align-items-center mt-4">
              <h5>Total Cost: €{totalCost}</h5>
              <div>
                <button
                  className="btn btn-secondary me-2"
                  onClick={handleCancel}
                >
                  Cancel
                </button>
                <button className="btn btn-primary" onClick={handleConfirm}>
                  Confirm
                </button>
              </div>
            </div>
          </>
        )}
      </div>
    </div>
  );
}
