import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import NavBar from "../../components/NavBar/NavBar";
import LoadingSpinner from "../../components/LoadingSpinner/LoadingSpinner";
import CarComponent from "../../components/CarComponent/CarComponent";
import { getAuctionById } from "../../api/auctionsService";
import { getCurrentUserId } from "../../api/authService";
import { CONSTANTS } from "../../consts";

import { Client } from "@stomp/stompjs";
import { showError } from "../../api/utils/swalHelpers";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import TokenManager from "../../api/utils/TokenManager";

export default function AuctionPage() {
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  const auctionId = searchParams.get("auctionId");

  const [stompClient, setStompClient] = useState(null);
  const [currentInputBid, setCurrentInputBid] = useState(0);

  const [isLoadingAuction, setLoadingAuction] = useState(true);
  const [auctionData, setAuctionData] = useState(null);
  const [carData, setCarData] = useState(null);
  const [bids, setBids] = useState([]);
  const [activeUsers, setActiveUsers] = useState([]);

  function getHighestBid() {
    return bids[0];
  }

  useEffect(() => {
    fetchAuction();
  }, []);

  const [timeLeft, setTimeLeft] = useState({
    total: "",
    hours: "",
    minutes: "",
    seconds: "",
  });

  function startExpiryTimer(auction) {
    const timer = setInterval(() => {
      const remainingTime = calculateTimeLeft(new Date(auction.endDateTime));
      setTimeLeft(remainingTime);

      if (remainingTime.total <= 0) {
        clearInterval(timer);
      }
      setLoadingAuction(false);
    }, 1000);
  }

  function calculateTimeLeft(expiry) {
    const now = new Date();
    const difference = new Date(expiry) - now;

    if (difference <= 0) {
      return { total: 0, hours: 0, minutes: 0, seconds: 0 };
    }

    const hours = Math.floor(difference / (1000 * 60 * 60));
    const minutes = Math.floor((difference / (1000 * 60)) % 60);
    const seconds = Math.floor((difference / 1000) % 60);

    return { total: difference, hours, minutes, seconds };
  }

  async function fetchAuction() {
    setLoadingAuction(true);
    const auction = await getAuctionById(auctionId);

    setAuctionData(auction);
    setCarData(auction.car);
    setBids(
      auction.bids.sort(function (a, b) {
        return b.id - a.id;
      })
    );
    setActiveUsers([...new Set(auction.bids.map((b) => b.user.name))]);

    startExpiryTimer(auction);

    connectWebSocket();
  }

  function connectWebSocket() {
    const client = new Client({
      brokerURL: `${CONSTANTS.BACKEND_WS_URL}/ws`,
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      connectHeaders: {
        userId: getCurrentUserId(),
        auctionId: auctionId,
      },
    });

    client.onConnect = () => {
      console.log("WebSocket connected!");

      client.subscribe(`/auction/${auctionId}/bids`, (message) => {
        const newBid = JSON.parse(message.body);

        console.log("received data from bids websocket: ", newBid);

        setBids((prevBids) => [newBid, ...prevBids]);
      });

      client.subscribe(`/auction/${auctionId}/new-user`, (message) => {
        const newUserName = message.body;

        console.log("received data from new-users websocket: ", newUserName);

        toast.success("New user viewing the auction: " + newUserName);
      });
    };

    client.activate();
    setStompClient(client);
  }

  async function placeBid() {
    if (currentInputBid < auctionData.minBidAmount) {
      showError("Bid amount cannot be less than the minimum bid amount!");
      return;
    }

    if (currentInputBid <= (bids[0]?.amount || 0)) {
      showError("Bid amount cannot be less than the highest bid!");
      return;
    }

    const bidRequest = {
      auctionId: +auctionId,
      userId: +getCurrentUserId(),
      bidAmount: currentInputBid,
    };

    stompClient.publish({
      destination: `/auction/bid`,
      headers: {
        accessToken: TokenManager.getAccessToken(),
      },
      body: JSON.stringify(bidRequest),
    });
  }
  const notify = () => toast("Wow so easy!");

  return (
    <div className="auction-page text-white">
      <NavBar bg="light" />
      <ToastContainer
        position="top-right"
        autoClose={1700}
        hideProgressBar={false}
        newestOnTop
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
        theme="colored"
      />{" "}
      <h2 className="text-center mt-5">Auction Details</h2>
      {isLoadingAuction ? (
        <LoadingSpinner />
      ) : (
        <>
          <h3 className="text-center m-4">Auction #{auctionData.id}</h3>
          <div className="d-flex justify-content-around align-items-center gap-25 p-3">
            <div className="round-edge p-4 light-gray-background text-white fb-45">
              <h3>{auctionData.description}</h3>
              <br></br>

              <h3>
                Remaining time:{" "}
                {timeLeft.total > 0 ? (
                  <span className="text-warning text-bold">
                    {timeLeft.hours}h {timeLeft.minutes}m {timeLeft.seconds}s
                  </span>
                ) : (
                  <span className="text-danger text-bold">
                    Auction has ended!
                  </span>
                )}
              </h3>

              <div className="d-flex gap-25">
                <h3 className="br-1 p-3">
                  Minimum bid <br></br>
                  <p className="text-warning text-bold text-center">
                    € {auctionData.minBidAmount}
                  </p>
                </h3>

                <h3 className="p-3 br-1">
                  Highest bid <br></br>
                  <p className="text-warning text-bold text-center">
                    € {getHighestBid()?.amount || 0}
                  </p>
                </h3>
                <h3 className="p-3">
                  Highest bidder<br></br>
                  <p
                    className={`text-${
                      getHighestBid()?.user.id == getCurrentUserId()
                        ? "success"
                        : "warning"
                    } text-bold`}
                  >
                    {" "}
                    {getHighestBid()?.user.name}
                  </p>
                </h3>
              </div>

              {timeLeft.total > 0 ? (
                <div className="d-flex gap-25">
                  <input
                    type="number"
                    className="form-control bid-input"
                    onChange={(e) => setCurrentInputBid(e.target.value)}
                  ></input>

                  {getHighestBid()?.user.id == getCurrentUserId() ? (
                    <button className="btn btn-white w-100 text-bold disabled place-bid-btn">
                      You have the highest bid
                    </button>
                  ) : (
                    <button
                      className="btn btn-white w-100 text-bold place-bid-btn"
                      onClick={placeBid}
                    >
                      &lt;--- BID NOW
                    </button>
                  )}
                </div>
              ) : (
                ""
              )}
            </div>

            <div className="fb-50">
              <br></br>
              {isLoadingAuction ? (
                <LoadingSpinner />
              ) : (
                <CarComponent car={carData} excludePrice={true} />
              )}
            </div>
          </div>

          <div className="d-flex justify-content-around align-items-center gap-25 p-3">
            <div className="round-edge p-4 light-gray-background text-white fb-45">
              <h3 className="text-center">Bidding History :</h3>
              <div className="w-90 m-auto overflow-scroll mh-40vh">
                <div className="row d-flex text-bold bb-1 pb-2">
                  <span className="fb-30 br-1">Time</span>
                  <span className="fb-30 br-1">User</span>
                  <span className="fb-30">Amount</span>
                </div>

                {bids.map((bid) => {
                  return (
                    <div
                      key={bid.id}
                      className="row d-flex text-bold bb-1 pb-2"
                    >
                      <span className="fb-30 br-1">
                        {bid.dateTime.replace("T", " ")}
                      </span>
                      <span className="fb-30 br-1">{bid.user.name}</span>
                      <span className="fb-30">€ {bid.amount}</span>
                    </div>
                  );
                })}
              </div>
            </div>

            <div className="round-edge p-3 light-gray-background text-white fb-45 text-center">
              <h3>
                Participating users:{" "}
                <span className="text-success">({activeUsers.length})</span>
              </h3>

              <div className="d-flex flex-column mh-40vh overflow-scroll">
                {activeUsers.map((user) => {
                  return (
                    <div
                      key={"active-user-" + user.replace(" ", "-")}
                      className="d-flex align-items-center bb-1 p-2 w-50 m-auto justify-content-center"
                    >
                      <i className="fa-regular text-big fa-circle-user"></i>
                      <span className="fs-25">{user}</span>
                    </div>
                  );
                })}
              </div>
            </div>
          </div>
        </>
      )}
    </div>
  );
}
