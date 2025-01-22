import { del, get, post, put } from "./baseService";


export const getCarReviews = async (carId) => {
    const responseData = await get(
        `reviews?carId=${carId}`
    );
    return responseData;
};

export const getUserReviews = async () => {
    const responseData = await get(
        `reviews/by-user`
    );
    return responseData;
};

export const deleteReview = async (reviewId) => {
    const responseData = await del(
        `reviews/${reviewId}`
    );
    return responseData;
};

export const updateReview = async (reviewData) => {
    const responseData = await put(
        `reviews/${reviewData.id}`,
        reviewData
    );
    return responseData;
};

export const createReview = async (reviewData) => {
    const responseData = await post(
        `reviews`,
        reviewData
    );
    return responseData;
};