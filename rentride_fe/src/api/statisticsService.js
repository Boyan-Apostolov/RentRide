import { get, post } from "./baseService";

export const getCarStatistics = async (carId) => {
    const responseData = await get(
        `statistics/by-car?carId=${carId}`
    );
    return responseData;
};

export const getGeneralStatistics = async () => {
    const responseData = await get(
        `statistics/general`
    );
    return responseData;
};

export const getMostBoughtDiscountPlans = async () => {
    const responseData = await get(
        `statistics/discounts`
    );
    return responseData;
};
export const getMostPopularCars = async () => {
    const responseData = await get(
        `statistics/cars`
    );
    return responseData;
};

export const getMostPopularTrips = async () => {
    const responseData = await get(
        `statistics/trips`
    );
    return responseData;
};

export const getBookingsPerMonth = async () => {
    const responseData = await get(
        `statistics/per-month`
    );
    return responseData;
};

export const getPopularCarsOverTime = async (fromDate, toDate) => {
    const params = new URLSearchParams();

    if (fromDate) params.append('startDateTime', fromDate + "T00:00:01");
    if (toDate) params.append('endDateTime', toDate + "T23:59:59");

    const responseData = await get(`statistics/popular-over-time?${params.toString()}`);
    return responseData;
};
