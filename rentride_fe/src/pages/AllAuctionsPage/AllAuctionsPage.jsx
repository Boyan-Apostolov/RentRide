import React, { useEffect, useState } from "react";
import { isCurrentUserInRole } from "../../api/authService";
import LoadingSpinner from "../../components/LoadingSpinner/LoadingSpinner";
import AllBookingsTable from "../../components/AllBookingsTable/AllBookingsTable";
import AllAuctionsTable from "../../components/AllAuctionsTable/AllAuctionsTable";
import {
  getAllAuctions,
  getAllAuctionsCount,
  getAllPagedAuctions,
} from "../../api/auctionsService";
import NavBar from "../../components/NavBar/NavBar";
import Pagination from "../../components/Pagination/Pagination";

export default function AllAuctionsPage() {
  const [auctions, setAuctions] = useState([]);
  const [isLoadingAuctions, setLoadingAuctions] = useState(true);
  const [totalAuctionsCount, setTotalAuctions] = useState(0);

  useEffect(() => {
    fetchAllAuctionsCount();
    fetchAuctionsData(0);
  }, []);

  async function fetchAllAuctionsCount() {
    const count = await getAllAuctionsCount();
    setTotalAuctions(count);
  }

  async function fetchAuctionsData(page) {
    setLoadingAuctions(true);
    const auctionsData = await getAllPagedAuctions(page);
    setLoadingAuctions(false);
    setAuctions(auctionsData);
  }

  return (
    <>
      <NavBar bg="light" />
      {isLoadingAuctions ? (
        <LoadingSpinner />
      ) : (
        <AllAuctionsTable auctions={auctions} />
      )}

      {totalAuctionsCount > 0 && (
        <Pagination
          textColor="white"
          onPageChange={fetchAuctionsData}
          totalCount={totalAuctionsCount}
        />
      )}
    </>
  );
}
