import React, { useState } from "react";
import { cancelBooking, getAllDamages } from "../../api/bookingService";
import { createPaymentRequest } from "../../api/paymentService";
import CarDamageReportPopup from "../CarDamageReportPopup/CarDamageReportPopup";
import RatingModal from "../RatingModal/RatingModal";
import { createReview } from "../../api/reviewService";
import { getCurrentUserId } from "../../api/authService";
import { showError } from "../../api/utils/swalHelpers";

export default function AllBookingsTable(props) {
  const [damageReportIsShown, setDamageReportIsShown] = useState(false);
  const [reviewModalIsShown, setReviewModalIsShown] = useState(false);

  const [currentReporableBooking, setCurrentReporableBooking] = useState({});

  function getStatusSpanBasedOnStatus(status) {
    let statusSpan = "<span";

    if (status === "Unpaid") {
      statusSpan +=
        " class='badge bg-danger'><i class='fa-solid fa-hand-holding-dollar'></i> ";
    } else if (status === "Paid") {
      statusSpan +=
        " class='badge bg-secondary'><i class='fa-solid fa-dollar-sign'></i>  ";
    } else if (status === "Active") {
      statusSpan +=
        " class='badge bg-warning'><i class='fa-solid fa-bolt'></i> ";
    } else if (status === "Finished") {
      statusSpan +=
        " class='badge bg-success'><i class='fa-solid fa-flag-checkered'></i> ";
    } else if (status === "Canceled") {
      statusSpan += " class='badge bg-danger'><i class='fa-solid fa-ban'></i> ";
    } else if (status === "Rated") {
      statusSpan +=
        " class='badge bg-success'><i class='fa-solid fa-check'></i> ";
    }

    return statusSpan + status + "</span>";
  }

  async function handleBookingCancel(bookingId) {
    const isConfirmed = confirm(
      "Are you sure you want to cancel this booking?"
    );
    if (!isConfirmed) return;

    await cancelBooking(bookingId);
    props.loadCallback();
  }

  async function submitReview(reviewData) {
    reviewData.bookingId = currentReporableBooking.id;
    reviewData.userId = getCurrentUserId();

    await createReview(reviewData);
    setReviewModalIsShown(false);
    props.loadCallback();
  }

  async function submitDamageReport(damages) {
    const isConfirmed = confirm("Are you sure you want to submit this report?");
    if (!isConfirmed) return;

    let totalToPay = damages.reduce((total, d) => total + d.cost, 0);

    totalToPay -=
      currentReporableBooking.coverage == "No"
        ? 0
        : currentReporableBooking.coverage == "Top"
        ? Math.round(totalToPay / 2)
        : totalToPay;

    if (totalToPay <= 0) {
      showError("Booking is with Premium coverage and user won't be charged");
      return;
    }

    await createPaymentRequest({
      description: `Found Damage on ${damages.map((d) => d.name).join(", ")}`,
      totalCost: totalToPay,
      userId: currentReporableBooking.user.id,
      bookingId: currentReporableBooking.id,
    });

    setDamageReportIsShown(false);
  }

  function handleBookingReview(booking) {
    setCurrentReporableBooking(booking);
    setReviewModalIsShown(true);
  }
  async function handleBookingRevision(booking) {
    setCurrentReporableBooking(booking);
    setDamageReportIsShown(true);
  }

  return (
    <div className="d-flex flex-column align-items-center">
      {damageReportIsShown ? (
        <CarDamageReportPopup
          onConfirm={submitDamageReport}
          onClose={() => setDamageReportIsShown(false)}
        />
      ) : (
        ""
      )}

      {reviewModalIsShown ? (
        <RatingModal
          onConfirm={submitReview}
          onClose={() => setReviewModalIsShown(false)}
        />
      ) : (
        ""
      )}

      <div className=" overflow-scroll w-90 round-edge m-3 p-3 light-gray-background text-white m-auto ">
        <h2 className="text-center">All Bookings</h2>
        {props.bookings.length === 0 ? (
          <span>No bookings yet...</span>
        ) : (
          <table className="table table-striped table-light round-edge">
            <thead>
              <tr>
                <th scope="col">#</th>
                <th scope="col">Status</th>
                <th scope="col">Start Date</th>
                <th scope="col">Trip</th>
                <th scope="col">End Date</th>
                <th scope="col">Car</th>
                <th scope="col">User</th>
                <th scope="col">Distance</th>
                <th scope="col">Total Price</th>
                <th scope="col">Actions</th>
              </tr>
            </thead>
            <tbody>
              {props.bookings.map((booking) => {
                return (
                  <tr key={booking.id}>
                    <th scope="row">{booking.id}</th>
                    <td
                      dangerouslySetInnerHTML={{
                        __html: getStatusSpanBasedOnStatus(
                          booking.bookingStatus
                        ),
                      }}
                    ></td>
                    <td>{booking.startDateTime.split("T")[0]}</td>
                    <td>
                      {booking.startCity.name} {"->"} {booking.endCity.name}{" "}
                    </td>
                    <td>{booking.endDateTime.split("T")[0]}</td>
                    <td>{booking.car.registrationNumber}</td>
                    <td>{booking.user.email}</td>
                    <td>{booking.distance} km</td>
                    <td>
                      <span>{booking.totalPrice} €</span>
                    </td>
                    <td>
                      {booking.bookingStatus == "Unpaid" ||
                      booking.bookingStatus == "Paid" ? (
                        <button
                          className="btn btn-danger"
                          onClick={() => handleBookingCancel(booking.id)}
                        >
                          <i className="fa-solid fa-ban" title="Cancel"></i>
                        </button>
                      ) : (
                        ""
                      )}

                      {booking.bookingStatus == "Finished" ? (
                        <>
                          <button
                            className="btn btn-warning"
                            onClick={() => handleBookingReview(booking)}
                          >
                            <i className="fa-solid fa-star" title="Review"></i>
                            {""}
                            Review
                          </button>
                        </>
                      ) : (
                        ""
                      )}

                      {booking.bookingStatus == "Finished" ||
                      booking.bookingStatus == "Rated" ? (
                        <>
                          <button
                            className="btn btn-primary"
                            onClick={() => handleBookingRevision(booking)}
                          >
                            <i
                              className="fa-solid fa-list-check"
                              title="Revise"
                            ></i>{" "}
                            Report
                          </button>
                        </>
                      ) : (
                        ""
                      )}
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}
