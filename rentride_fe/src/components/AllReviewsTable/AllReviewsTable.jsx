import React from "react";
import ReviewComponent from "../ReviewComponent/ReviewComponent";

export default function AllReviewsTable({ reviews, onUpdate }) {
  return (
    <div className="w-90 p-3 m-4 m-auto light-gray-background">
      <h2 className="text-white text-center">All reviews</h2>

      <div className="d-flex flex-wrap">
        {reviews.map((review) => {
          return (
            <div className="m-auto" key={`${review.id}-holder`}>
              <h5>{`${review.booking.car.make} ${review.booking.car.model}`}</h5>
              <ReviewComponent review={review} onUpdate={onUpdate} />
              <hr className="white-hr"></hr>
            </div>
          );
        })}
      </div>
    </div>
  );
}
