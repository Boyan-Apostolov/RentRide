import React from "react";
import { useNavigate } from "react-router-dom";
import { getCurrentUserId } from "../../api/authService";

export default function AllAuctionsTable({ auctions }) {
  const navigate = useNavigate();
  return (
    <div className="w-90 round-edge m-3 p-3  m-auto light-gray-background text-white ">
      <h2 className="text-center">All Auctions</h2>
      <table className="table table-striped table-light round-edge">
        <thead>
          <tr>
            <th scope="col">#</th>
            <th scope="col">Description</th>
            <th scope="col">End Date</th>
            <th scope="col">Car</th>
            <th scope="col">Winner</th>
            <th scope="col">Join</th>
          </tr>
        </thead>
        <tbody>
          {auctions.map((auction) => {
            return (
              <tr key={auction.id}>
                <th scope="row">{auction.id}</th>
                <td>{auction.description}</td>
                <td>{auction.endDateTime.replace("T", " ")}</td>
                <td>
                  {auction.car.make} {auction.car.model}
                </td>
                <td>
                  {auction.winnerUser != null ? (
                    <span className="text-success">
                      {auction.winnerUser?.name}
                    </span>
                  ) : (
                    <span className="text-warning">Not yet decided</span>
                  )}
                </td>
                <td>
                  {auction.winnerUser == null ? (
                    <button
                      className="btn btn-success"
                      onClick={() =>
                        navigate(`/auction?auctionId=${auction.id}`)
                      }
                    >
                      Join <i className="fa-solid fa-play"></i>
                    </button>
                  ) : auction.winnerUser.id == getCurrentUserId() &&
                    auction.canBeClaimed == 1 ? (
                    <button
                      className="btn btn-primary"
                      onClick={() =>
                        navigate(`/claim-auction?auctionId=${auction.id}`)
                      }
                    >
                      Claim <i className="fa-solid fa-hand-holding-heart"></i>
                    </button>
                  ) : (
                    "Auction is over"
                  )}
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}
