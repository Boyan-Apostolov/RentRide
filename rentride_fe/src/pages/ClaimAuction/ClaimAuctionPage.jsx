import React, { useEffect, useState } from "react";
import NavBar from "../../components/NavBar/NavBar";
import { useNavigate, useSearchParams } from "react-router-dom";
import { getAuctionById } from "../../api/auctionsService";
import LoadingSpinner from "../../components/LoadingSpinner/LoadingSpinner";
import SearchFilter from "../../components/SearchFilter/SearchFilter";
import { claimAuctionBooking } from "../../api/bookingService";

export default function ClaimAuctionPage() {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const auctionId = searchParams.get("auctionId");

  const [isLoadingAuction, setIsLoadingAuction] = useState(true);
  const [auctionData, setAuctionData] = useState({});

  async function fetchAuction() {
    setIsLoadingAuction(true);

    const data = await getAuctionById(auctionId);
    setAuctionData(data);

    setIsLoadingAuction(false);
  }

  useEffect(() => {
    fetchAuction();
  }, []);

  async function overrideSearchFunction(formData) {
    await claimAuctionBooking({
      auctionId: auctionId,
      carId: auctionData.car.id,
      coverage: 2,
      fromCityId: formData.fromCity,
      toCityId: formData.toCity,
      startDateTime: `${formData.fromDate}T${formData.fromTime}`,
      endDateTime: `${formData.toDate}T${formData.toTime}`,
    });

    navigate("/my-profile");
  }

  return (
    <>
      <NavBar bg="light" />
      <div className="col-3 round-edge m-3 mt-5 p-3  m-auto light-gray-background text-white ">
        <h2 className="text-center">Claim auction #{auctionId}</h2>
        {isLoadingAuction ? (
          <LoadingSpinner />
        ) : (
          <div>
            <h3 className="text-center">
              Won car:
              <h3 className="text-primary">
                {auctionData.car.make} {auctionData.car.model} (
                {auctionData.car.registrationNumber})
              </h3>
            </h3>
            <SearchFilter
              isForAuctionClaim={true}
              overrideSearchFunction={overrideSearchFunction}
            />
          </div>
        )}
      </div>
    </>
  );
}
