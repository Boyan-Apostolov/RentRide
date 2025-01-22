import { get, post } from "./baseService";

export const getAllAuctions = async () => {
    const responseData = await get(`auctions`);
    return responseData;
};

export const getAllAuctionsCount = async () => {
    const responseData = await get(`auctions/count`);
    return responseData;
};

export const getAllPagedAuctions = async (page) => {
    const responseData = await get(`auctions/paged?page=${page}`);
    return responseData;
};

export const createAuction = async (auctionObj) => {
    const responseData = await post(`auctions`, auctionObj);
    return responseData;
}

export const getAuctionById = async (auctionId) => {
    const responseData = await get(`auctions/${auctionId}`);
    return responseData;
}