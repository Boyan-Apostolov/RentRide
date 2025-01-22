import React, { useState } from "react";
import { deleteReview, updateReview } from "../../api/reviewService";
import { getCurrentUserId, isCurrentUserInRole } from "../../api/authService";

export default function ReviewComponent(props) {
  const originalReview = props.review;
  const [review, setReview] = useState(originalReview);

  const userCanEditReview =
    isCurrentUserInRole("ADMIN") || review.user.id == getCurrentUserId();

  const [isEditMode, setIsEditMode] = useState(false);

  async function handleReviewUpdate() {
    if (review.text === "") return alert("Review text cannot be empty");

    setIsEditMode(false);
    await updateReview(review);
    props.onUpdate();
  }

  async function handleReviewDelete() {
    if (!confirm("Are you sure you want to delete this review?")) return;

    await deleteReview(review.id);
    props.onUpdate();
  }

  return (
    <>
      <div className="round-edge m-3 p-3 gap-25 fb-40 very-light-gray-background text-white d-flex justify-content-evenly">
        <div className="fb-50 d-flex flex-column ml-2">
          <div className="d-flex gap-25">
            <div className="fb-30">
              <img
                src={`https://api.dicebear.com/9.x/rings/svg?seed=${review.user.name}`}
                alt=""
              />
            </div>
            <div className="d-flex flex-column">
              <span>
                By:
                <span className="text-bold m-1">{review.user.name}</span>
              </span>
              <span>
                From:
                <span className="text-bold m-1">
                  {review.createdOn.split("T")[0]}
                </span>
              </span>
              <span>
                Travelled:
                <span className="text-bold m-1">{review.booking.distance}</span>
                km
              </span>
            </div>
          </div>
          <div className="text-bold m-1">
            {isEditMode ? (
              <input
                className="form-control"
                required
                value={review.text}
                onChange={(e) => {
                  setReview({
                    ...review,
                    text: e.target.value,
                  });
                }}
              ></input>
            ) : (
              review.text
            )}
          </div>
        </div>

        <div className="fb-30 d-flex flex-column">
          <span>Value for money:</span>
          {isEditMode ? (
            <input
              type="range"
              className="form-range"
              min="0"
              max="5"
              value={review.valueForMoney}
              onChange={(e) => {
                setReview({
                  ...review,
                  valueForMoney: +e.target.value,
                });
              }}
            ></input>
          ) : (
            <progress
              value={review.valueForMoney}
              className="w-100"
              title={`${review.valueForMoney}/5`}
              max="5"
            ></progress>
          )}
          <span>Car condition:</span>
          {isEditMode ? (
            <input
              type="range"
              className="form-range"
              min="0"
              max="5"
              value={review.carCondition}
              onChange={(e) => {
                setReview({
                  ...review,
                  carCondition: +e.target.value,
                });
              }}
            ></input>
          ) : (
            <progress
              value={review.carCondition}
              className="w-100"
              title={`${review.carCondition}/5`}
              max="5"
            ></progress>
          )}
          <span>Car speed:</span>

          {isEditMode ? (
            <input
              type="range"
              className="form-range"
              min="0"
              max="5"
              value={review.carSpeed}
              onChange={(e) => {
                setReview({
                  ...review,
                  carSpeed: +e.target.value,
                });
              }}
            ></input>
          ) : (
            <progress
              value={review.carSpeed}
              className="w-100"
              title={`${review.carSpeed}/5`}
              max="5"
            ></progress>
          )}
        </div>

        {userCanEditReview ? (
          <div className="fb-10 d-flex gap-25">
            {isEditMode ? (
              <>
                <button
                  className="btn btn-success"
                  onClick={handleReviewUpdate}
                >
                  <i className="fa-solid fa-check" title="Confirm"></i>
                </button>

                <button
                  className="btn btn-secondary"
                  onClick={() => {
                    setIsEditMode(!isEditMode);
                    setReview(originalReview);
                  }}
                >
                  <i className="fa-solid fa-xmark" title="Cancel"></i>
                </button>
              </>
            ) : (
              <>
                <button
                  className="btn btn-warning"
                  onClick={() => setIsEditMode(!isEditMode)}
                >
                  <i className="fa-solid fa-pen" title="Edit"></i>
                </button>

                <button className="btn btn-danger" onClick={handleReviewDelete}>
                  <i className="fa-solid fa-trash" title="Delete"></i>
                </button>
              </>
            )}
          </div>
        ) : (
          ""
        )}
      </div>
    </>
  );
}
