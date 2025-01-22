import { get, post, put, del } from "./baseService";

export const getAllCarsCount = async () => {
    const responseData = await get(`cars/count`);
    return responseData;
};

export const getAllCars = async () => {
    const responseData = await get(`cars`);
    return responseData;
};

export const getAllPagedCars = async (page) => {
    const responseData = await get(`cars/paged?page=${page}`);
    return responseData;
};


export const getExclusiveCars = async () => {
    const responseData = await get(`cars/exclusive`);
    return responseData;
};

export const getCarDetails = async (carId) => {
    const responseData = await get(`cars/${carId}`);
    return responseData;
};

export const getAvailableCars = async (fromCity, fromDate, fromTime, toCity, toDate, toTime, features) => {
    const responseData = await post(`cars/availableCars`, {
        fromCity: fromCity,
        toCity: toCity,
        fromDateTime: `${fromDate}T${fromTime}`,
        toDateTime: `${toDate}T${toTime}`,
        selectedFeatures: features ? features.split(",").map(f => parseInt(f)) : null
    });
    return responseData;
};

export const getCarFilters = async () => {
    const carFeatures = await get(`cars/features`);

    const groupedFeatures = carFeatures.reduce((acc, item) => {
        if (!acc[item.featureType]) {
            acc[item.featureType] = [];
        }
        acc[item.featureType].push(item);
        return acc;
    }, {});

    return groupedFeatures;
};

export const createCar = async (carModel) => {
    const responseData = await post(`cars`, carModel);
    return responseData;
};

export const updateCar = async (carModel) => {
    const responseData = await put(`cars/${carModel.id}`, carModel);
    return responseData;
};

export const deleteCar = async (carId) => {
    const responseData = await del(`cars/${carId}`);
    return responseData;
};