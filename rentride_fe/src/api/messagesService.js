import { get, post, put } from "./baseService.js";

export const getAllMessages = async () => {
    const responseData = await get(`messages`);
    return responseData;
};

export const submitMessage = async (messagObj) => {
    const responseData = await post(`messages`, messagObj);
    return responseData;
};


export const answerMessage = async (messageId, answerContent) => {
    const responseData = await put(`messages/${messageId}`, answerContent);
    return responseData;
};