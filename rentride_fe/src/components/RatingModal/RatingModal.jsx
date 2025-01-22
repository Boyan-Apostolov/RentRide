import React, { useState } from "react";
import "./RatingModal.css";

export default function RatingModal(props) {
  const [valueForMoney, setValueForMoney] = useState(0);
  const [carSpeed, setCarSpeed] = useState(0);
  const [carCondition, setCarCondition] = useState(0);
  const [text, settext] = useState("");

  const handleStarClick = (setRating, rating) => {
    setRating(rating);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (text == "") return alert("Please enter some text to the review");

    props.onConfirm({
      valueForMoney,
      carSpeed,
      carCondition,
      text,
    });
  };

  const handleCancel = () => {
    props.onClose();
  };

  const renderStars = (rating, setRating) => {
    return Array(5)
      .fill(0)
      .map((_, index) => (
        <span
          key={index}
          onClick={() => handleStarClick(setRating, index + 1)}
          className={`star ${index < rating ? "selected" : ""}`}
        >
          ★
        </span>
      ));
  };

  return (
    <div className="popup-overlay d-flex justify-content-center align-items-center">
      <div className="light-gray-background text-white popup-content p-4 bg-light rounded">
        <div className="">
          <div className="text-center mb-4">
            <h3>Add a review</h3>
            <img
              src="/src/assets/rating.png"
              alt="Thumb Icon"
              className="thumb-icon mb-2 w-50"
            />
          </div>

          <div className="form-group mb-3">
            <label>Value for Money</label>
            <div>{renderStars(valueForMoney, setValueForMoney)}</div>
          </div>

          <div className="form-group mb-3">
            <label>Car Speed</label>
            <div>{renderStars(carSpeed, setCarSpeed)}</div>
          </div>

          <div className="form-group mb-3">
            <label>Car Condition</label>
            <div>{renderStars(carCondition, setCarCondition)}</div>
          </div>

          <textarea
            placeholder="How was the ride?"
            value={text}
            onChange={(e) => settext(e.target.value)}
            className="form-control mb-4"
            rows="4"
          />

          <div className="d-flex justify-content-between">
            <button onClick={handleCancel} className="btn btn-secondary">
              Close
            </button>

            <button onClick={handleSubmit} className="btn btn-success">
              Save review
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
