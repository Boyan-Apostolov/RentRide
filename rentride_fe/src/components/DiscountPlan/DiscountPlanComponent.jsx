import React, { useEffect, useState } from "react";
import { deleteDiscountPlan } from "../../api/discountPlanService";
import { createDiscountPaymentLink } from "../../api/paymentService";
import { createDiscountPlanPurchase } from "../../api/discountPlanService";
import LoadingSpinner from "../LoadingSpinner/LoadingSpinner";
import {
  getCurrentUserId,
  isCurrentUserInRole,
  isUserLoggedIn,
} from "../../api/authService";
import { NavLink } from "react-router-dom";

import { isDiscountPlanBoughtBySessionUser } from "../../api/discountPlanService";

export default function DiscountPlanComponent(props) {
  const [isLoadingPayment, setIsLoadingPayment] = useState(false);
  const [discountPlanIsBought, setIsDiscountPlanBoughtBySessionUser] =
    useState(true);
  const [isLoadingDiscountPlanBought, setIsLoadingDiscountPlanBought] =
    useState(true);

  const discountPlan = props.discountPlan;
  console.log(discountPlan);

  const colors = [
    "gradient-primary",
    "gradient-secondary",
    "gradient-success",
    "gradient-danger",
    "gradient-warning",
    "gradient-info",
    "gradient-dark",
    "gradient-purple",
    "gradient-teal",
    "gradient-rose",
  ];

  function getRandomColor() {
    const randomIndex = Math.floor(Math.random() * colors.length);
    return colors[randomIndex];
  }

  async function checkIfDiscountPlanBought() {
    setIsLoadingDiscountPlanBought(true);

    if (getCurrentUserId() == null) return;

    const isBought = await isDiscountPlanBoughtBySessionUser(discountPlan.id);

    setIsDiscountPlanBoughtBySessionUser(isBought.bought);
    setIsLoadingDiscountPlanBought(false);
  }

  async function handleDiscountPlanDelete() {
    const isConfirmed = confirm(
      "Are you sure you want to delete this discount plan?"
    );
    if (!isConfirmed) return;

    await deleteDiscountPlan(discountPlan.id);
    props.loadCallback();
  }

  async function handleDiscountPlanPay() {
    setIsLoadingPayment(true);
    const paymentLink = await createDiscountPaymentLink({
      userId: getCurrentUserId(),
      discountPlanId: discountPlan.id,
    });

    document.location.href = paymentLink;
  }

  useEffect(() => {
    checkIfDiscountPlanBought();
  }, []);

  return (
    <div
      className={`round-edge border-white ${getRandomColor()} text-white d-flex flex-column  align-items-center fb-40 p-3 justify-content-between`}
    >
      {isCurrentUserInRole("ADMIN") ? (
        <button
          className="btn btn-danger btn-small ml-auto"
          onClick={handleDiscountPlanDelete}
        >
          <i className="fa-solid fa-trash"></i>
        </button>
      ) : (
        ""
      )}
      <div className="d-flex justify-content-between gap-25">
        <div className="fb-40">
          <h2 className="text-bold">{discountPlan.title}</h2>
          <span>{discountPlan.description}</span>
        </div>
        <div className="d-flex flex-column align-items-center">
          <span className="text-big text-bold">
            - {discountPlan.discountValue} %
          </span>
          <span>DISCOUNT</span>
        </div>
      </div>
      {isLoadingPayment ? (
        <LoadingSpinner />
      ) : !isUserLoggedIn() ? (
        <NavLink key="1" to="/login" className="nav-link text-white">
          <button className="btn round-edge btn-primary text-bold bg-primary text-white border-white">
            Login to buy
          </button>
        </NavLink>
      ) : discountPlanIsBought ? (
        <button className="disabled btn btn-small border-white btn-secondary">
          Already bought
        </button>
      ) : (
        <button
          className="btn btn-primary border-white"
          onClick={handleDiscountPlanPay}
        >
          Buy - € {discountPlan.price}
        </button>
      )}
    </div>
  );
}
