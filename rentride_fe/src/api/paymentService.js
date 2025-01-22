import { get, post } from "./baseService";

export const getPaymentLink = async (bookingId) => {
    const paymentLink = await get(`payments/pay?bookingId=${bookingId}`);
    return paymentLink;
};

export const createPaymentRequest = async (paymentRequestData) => {
    const paymentLink = await post(`payments/create-request`, paymentRequestData);
    return paymentLink;
};

export const createDiscountPaymentLink = async (paymentRequestData) => {
    const paymentLink = await post(`payments/create-discount-link`, paymentRequestData);
    return paymentLink;
};

export const getUserPayments = async () => {
    const paymentLink = await get(`payments/by-user`);
    return paymentLink;
};

export const getAllPayments = async () => {
    const paymentLink = await get(`payments`);
    return paymentLink;
};

export const getPagedUserPayments = async (page) => {
    const paymentLink = await get(`payments/paged?page=${page}`);
    return paymentLink;
};
export const getAllPaymentsCount = async () => {
    const paymentLink = await get(`payments/count`);
    return paymentLink;
};

export const updatePayment = async (responseType, stripeSessionId, paymentType, entityId) => {
    const responseData = await get(`payments/${responseType}?sessionId=${stripeSessionId}&paymentType=${paymentType}&entityId=${entityId}`);
    return responseData;
};