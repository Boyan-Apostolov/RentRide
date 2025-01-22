import axios from "axios";
import { CONSTANTS } from "../consts";
import { get, post } from "./baseService";

export const getRouteData = async (fromCity, toCity) => {

    const responseData = await get(`cities/route?fromCity=${fromCity}&toCity=${toCity}`);

    return {
        distance: parseFloat(responseData.distance),
        time: parseFloat(responseData.time),
        imgUrl: responseData.imgUrl,
        fromCity,
        toCity
    };
};

export const lookupCity = async (cityName) => {
    const responseData = await get(
        `cities/lookupCity?cityName=${cityName}`
    );
    return responseData;
};

export const createCity = async (cityData, depotAddress) => {
    const responseData = await post(`cities`, {
        name: cityData.city,
        lat: cityData.lat,
        lon: cityData.lon,
        depoAddress: depotAddress
    });
    return responseData;
};

export const getCities = async () => {
    const responseData = await get(`cities`);
    return responseData;
};

export const getCity = async (cityId) => {
    const responseData = await get(`cities/${cityId}`);
    return responseData;
};