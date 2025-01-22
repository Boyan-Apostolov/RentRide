import React, { useEffect, useState } from "react";
import { getDiscountPlans } from "../../api/discountPlanService";
import NavBar from "../../components/NavBar/NavBar";
import LoadingSpinner from "../../components/LoadingSpinner/LoadingSpinner";
import DiscountPlanComponent from "../../components/DiscountPlan/DiscountPlanComponent";
export default function DiscountPlansPage() {
  const [discountPlans, setDiscountPlans] = useState([]);
  const [isLoadingDiscountPlans, setLoadingDiscountPlans] = useState(true);

  async function fetchDiscountPlans() {
    setLoadingDiscountPlans(true);
    const discountPlansData = await getDiscountPlans();
    setDiscountPlans(discountPlansData);
    setLoadingDiscountPlans(false);
  }

  useEffect(() => {
    fetchDiscountPlans();
  }, []);

  return (
    <>
      <NavBar bg="light" />
      <div className="d-flex flex-column align-items-center">
        <div className="w-90 round-edge m-3 p-3 light-gray-background text-white">
          <h1 className="text-center">Discount Plans</h1>

          <div className="d-flex mt-5 gap-25 justify-content-center flex-wrap">
            {isLoadingDiscountPlans ? (
              <LoadingSpinner />
            ) : (
              discountPlans.map((discountPlan) => (
                <DiscountPlanComponent
                  key={discountPlan.id}
                  loadCallback={fetchDiscountPlans}
                  discountPlan={discountPlan}
                />
              ))
            )}
          </div>
        </div>
      </div>
    </>
  );
}
