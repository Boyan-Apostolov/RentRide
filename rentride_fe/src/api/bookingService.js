import { get, post } from "./baseService";

export const getPriceDetails = async (obj) => {
    const responseData = await post(`bookings/calculate-cost`, obj);
    return responseData;
};

export const createBooking = async (bookingObj) => {
    const responseData = await post(`bookings`, bookingObj);
    return responseData.bookingId;
};

export const claimAuctionBooking = async (bookingObj) => {
    const responseData = await post(`bookings/claim`, bookingObj);
    return responseData.bookingId;
};


export const getAllBookings = async () => {
    const responseData = await get(`bookings`);
    return responseData;
};

export const getPagedUserBookings = async (page) => {
    const responseData = await get(`bookings/paged?page=${page}`);
    return responseData;
};
export const getAllBookingsCount = async () => {
    const count = await get(`bookings/count`);
    return count;
};

export const getCarBookings = async (carId) => {
    const responseData = await get(`bookings/by-car?carId=${carId}`);
    return responseData;
};


export const getCarHistoryMap = async (carId) => {
    const responseData = await get(`bookings/by-car-map?carId=${carId}`);
    return responseData;
};

export const getAllDamages = async () => {
    const responseData = await get(`bookings/damages`);
    return responseData;
};

export const getUserBookings = async () => {
    const responseData = await get(`bookings/by-user`);
    return responseData;
};

export const cancelBooking = async (bookingId) => {
    const responseData = await get(`bookings/cancel?bookingId=${bookingId}`);
    return responseData;
};