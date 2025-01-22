import { del, get, post } from "./baseService";

export const craeteDiscountPlan = async (data) => {
    const responseData = await post(`discountPlans`, data);
    return responseData;
};

export const getDiscountPlans = async () => {
    const responseData = await get(`discountPlans`);
    return responseData;
};

export const deleteDiscountPlan = async (discountPlanId) => {
    const responseData = await del(`discountPlans/${discountPlanId}`);
    return responseData;
};

export const createDiscountPlanPurchase = async (discountPlanData) => {
    const responseData = await post(`discountPlans/purchase`, discountPlanData);
    return responseData;
}

export const isDiscountPlanBoughtBySessionUser = async (discountPlanId) => {
    const responseData = await get(`discountPlans/is-bought?id=${discountPlanId}`);
    return responseData;
}
