import React from "react";
import LoadingSpinner from "../../components/LoadingSpinner/LoadingSpinner";
import { useSearchParams } from "react-router-dom";
import { updatePayment } from "../../api/paymentService";

export default function PaymentHandlerPage() {
  const [searchParams, setSearchParams] = useSearchParams();

  async function handlePayment() {
    const paymentType = searchParams.get("paymentType");
    const responseType = searchParams.get("responseType");
    const stripeSessionId = searchParams.get("sessionId");
    const entityId = searchParams.get("entityId");

    try {
      await updatePayment(responseType, stripeSessionId, paymentType, entityId);
    } catch (err) {
      console.log("payment cancelled: " + err.message);
    } finally {
      document.location.href = "/";
    }
  }
  handlePayment();
  return <LoadingSpinner />;
}
