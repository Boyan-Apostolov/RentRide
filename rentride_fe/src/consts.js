const BACKEND_URL = import.meta.env.VITE_BACKEND_URL;
const BACKEND_WS_URL = import.meta.env.VITE_BACKEND_WS_URL;
const AIVEN_API_TOKEN = import.meta.env.VITE_AIVEN_API_TOKEN;
const GOOGLE_CLIENT_ID = import.meta.env.VITE_GOOGLE_CLIENT_ID;
const DEFAULT_PAGE_SIZE = 5;
const AUTH_TOKEN_EXPIRY_MINS = 9;

const featureIcons = {
    Seats: "fa-solid fa-chair",
    Doors: "fa-solid fa-door-open",
    Transmission: "fa-solid fa-wand-sparkles",
    Bonus: "fa-solid fa-up-right-from-square",
    Fuel: "fa-solid fa-gas-pump",
    Car: "fa-solid fa-car-side"
};

const statisticsIcon = {
    appointment: "fa-solid fa-calendar-check",
    distance: "fa-solid fa-route",
    revenue: "fa-solid fa-money-bill-trend-up",
    rating: "fa-solid fa-star-half-stroke",
    users: "fa-solid fa-users"
}

export const CONSTANTS = {
    BACKEND_URL,
    BACKEND_WS_URL,
    featureIcons,
    statisticsIcon,
    DEFAULT_PAGE_SIZE,
    AUTH_TOKEN_EXPIRY_MINS,
    AIVEN_API_TOKEN,
    GOOGLE_CLIENT_ID
};