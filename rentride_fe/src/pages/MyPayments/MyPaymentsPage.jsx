import React, { useState, useEffect } from "react";
import {
  getPagedUserPayments,
  getAllPaymentsCount,
  getAllPayments,
} from "../../api/paymentService";
import NavBar from "../../components/NavBar/NavBar";
import LoadingSpinner from "../../components/LoadingSpinner/LoadingSpinner";
import AllPaymentsTable from "../../components/AllPaymentsTable/AllPaymentsTable";
import Pagination from "../../components/Pagination/Pagination";

export default function MyPaymentsPage() {
  const [payments, setPayments] = useState([]);
  const [isLoadingPayments, setLoadingPayments] = useState(true);

  async function fetchPaymentData() {
    setLoadingPayments(true);
    const paymentsData = await getAllPayments();

    setPayments(paymentsData);
    setLoadingPayments(false);
  }

  useEffect(() => {
    fetchPaymentData();
  }, []);

  return (
    <>
      <NavBar />
      <div className="d-flex flex-column align-items-center">
        {isLoadingPayments ? (
          <LoadingSpinner />
        ) : (
          <AllPaymentsTable payments={payments} />
        )}
      </div>
    </>
  );
}
